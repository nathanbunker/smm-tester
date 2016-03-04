package org.immunizationsoftware.dqa.mover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.immunizationsoftware.dqa.tester.connectors.Connector;

public class RxaFilter
{
  public Map<Connector, String> filter(String message, List<Connector> connectorList) {
    Set<String> rxa11Set = discoverRxa11(message, connectorList);
    Map<String, StringBuilder> messageTextMap = new HashMap<String, StringBuilder>();
    for (String rxa11 : rxa11Set) {
      messageTextMap.put(rxa11, new StringBuilder());
    }
    BufferedReader in = new BufferedReader(new StringReader(message));
    StringBuilder rxaPart = new StringBuilder();
    String line;
    String rxa11 = "";
    String rxa9 = "";
    boolean head = true;
    try {
      while ((line = in.readLine()) != null) {
        if (line.startsWith("ORC")) {
          addRxaPart(messageTextMap, rxaPart, rxa11, rxa9);
          rxa11 = "";
          rxa9 = "";
          head = false;
          rxaPart = new StringBuilder();
        } else if (line.startsWith("RXA")) {
          String[] parts = line.split("\\|");
          if (parts.length > 9) {
            rxa9 = clean(parts[9]);
            if (parts.length > 11) {
              rxa11 = readRxa11(parts);
            }
          }
        }
        if (head) {
          for (StringBuilder sb : messageTextMap.values()) {
            sb.append(line);
            sb.append("\r");
          }
        } else {
          rxaPart.append(line);
          rxaPart.append("\r");
        }

      }
      addRxaPart(messageTextMap, rxaPart, rxa11, rxa9);
      in.close();
    } catch (IOException ioe) {
      // should not happen while reading string
      ioe.printStackTrace();
    }

    Map<Connector, String> connectorMap = new HashMap<Connector, String>();
    for (Connector connector : connectorList) {
      if (messageTextMap.containsKey(connector.getRxaFilterFacilityId())) {
        connectorMap.put(connector, messageTextMap.get(connector.getRxaFilterFacilityId()).toString());
      }
    }
    return connectorMap;
  }

  public void addRxaPart(Map<String, StringBuilder> messageTextMap, StringBuilder rxaPart, String rxa11, String rxa9) {
    if (rxaPart.length() > 0) {
      if (rxa9.equals("00") && messageTextMap.containsKey(rxa11)) {
        StringBuilder sb = messageTextMap.get(rxa11);
        sb.append(rxaPart);
      } else {
        for (StringBuilder sb : messageTextMap.values()) {
          sb.append(rxaPart);
        }
      }
    }
  }

  public Set<String> discoverRxa11(String message, List<Connector> connectorList) {
    Set<String> rxa11Set = new HashSet<String>();
    BufferedReader in = new BufferedReader(new StringReader(message));
    Map<String, StringBuilder> messageTextMap = new HashMap<String, StringBuilder>();
    String line;
    try {
      while ((line = in.readLine()) != null) {
        if (line.startsWith("RXA")) {
          String[] parts = line.split("\\|");
          if (parts.length > 9) {
            String rxa9 = clean(parts[9]);
            if (rxa9.equals("00") && parts.length > 11) {
              String rxa11 = readRxa11(parts);
              if (!rxa11.equals("")) {
                for (Connector connector : connectorList) {
                  if (connector.getRxaFilterFacilityId().equalsIgnoreCase(rxa11)) {
                    rxa11Set.add(rxa11);
                    break;
                  }
                }
              }
            }
          }
        }
      }
      in.close();
    } catch (IOException ioe) {
      // should not happen while reading string
      ioe.printStackTrace();
    }

    return rxa11Set;
  }

  public String readRxa11(String[] parts) {
    String rxa11 = parts[11];
    int pos = rxa11.indexOf("^");
    if (pos >= 0) {
      pos = rxa11.indexOf("^", pos + 1);
      if (pos >= 0) {
        pos = rxa11.indexOf("^", pos + 1);
        if (pos >= 0) {
          rxa11 = clean(rxa11.substring(pos + 1));
        }
      }
    }
    return rxa11;
  }

  private String clean(String s) {
    if (s == null || s.equals("")) {
      return s;
    }
    int pos = s.indexOf("^");
    if (pos < 0) {
      return s;
    }
    return s.substring(0, pos);
  }
}
