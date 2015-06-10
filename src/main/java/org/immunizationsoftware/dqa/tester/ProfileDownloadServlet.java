package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.profile.ProfileField;
import org.immunizationsoftware.dqa.tester.profile.ProfileLine;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsageValue;

public class ProfileDownloadServlet extends ClientServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/cvs;charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"SMM Requirement Test Profiles.csv\"");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      Authenticate.User user = (Authenticate.User) session.getAttribute("user");

      PrintWriter out = response.getWriter();
      try {
        initProfileManager(false);
        Map<ProfileField, Map<ProfileUsage, ProfileUsageValue>> masterMap = new HashMap<ProfileField, Map<ProfileUsage, ProfileUsageValue>>();
        List<ProfileUsage> profileUsageList = profileManager.getProfileUsageList();
        for (int i = 0; i < (profileUsageList.size() - 1); i++) {
          ProfileUsage profileUsage = profileUsageList.get(i);
          for (ProfileField profileField : profileManager.getProfileFieldList()) {
            ProfileUsageValue profileUsageValue = profileUsage.getProfileUsageValueMap().get(profileField);
            if (profileUsageValue != null) {
              Map<ProfileUsage, ProfileUsageValue> profileUsageValueMap = masterMap.get(profileField);
              if (profileUsageValueMap == null) {
                profileUsageValueMap = new HashMap<ProfileUsage, ProfileUsageValue>();
                masterMap.put(profileField, profileUsageValueMap);
              }
              profileUsageValueMap.put(profileUsage, profileUsageValue);
            }
          }
        }
        out.print(makeCsv("Category"));
        out.print(makeCsv("Label"));
        out.print(makeCsv("Version"));
        out.print(makeCsv("Type"));
        for (ProfileField profileField : profileManager.getProfileFieldList()) {
          Map<ProfileUsage, ProfileUsageValue> profileUsageValueMap = masterMap.get(profileField);
          if (profileUsageValueMap != null) {
            out.print(makeCsv(profileField.getFieldName()));
          }
        }
        out.println();
        for (int i = 0; i < profileUsageList.size(); i++) {
          ProfileUsage profileUsage = profileUsageList.get(i);
          {
            out.print(makeCsv(profileUsage.getCategory()));
            out.print(makeCsv(profileUsage.getLabel()));
            out.print(makeCsv(profileUsage.getVersion()));
            out.print(makeCsv("Usage"));
            for (ProfileField profileField : profileManager.getProfileFieldList()) {
              Map<ProfileUsage, ProfileUsageValue> profileUsageValueMap = masterMap.get(profileField);
              if (profileUsageValueMap != null) {
                ProfileUsageValue profileUsageValue = profileUsageValueMap.get(profileUsage);
                if (profileUsageValue == null) {
                  out.print(",");
                } else {
                  out.print(makeCsv(profileUsageValue.getUsage()));
                }
              }
            }
            out.println();
          }
          {
            out.print(makeCsv(profileUsage.getCategory()));
            out.print(makeCsv(profileUsage.getLabel()));
            out.print(makeCsv(profileUsage.getVersion()));
            out.print(makeCsv("Value"));
            for (ProfileField profileField : profileManager.getProfileFieldList()) {
              Map<ProfileUsage, ProfileUsageValue> profileUsageValueMap = masterMap.get(profileField);
              if (profileUsageValueMap != null) {
                ProfileUsageValue profileUsageValue = profileUsageValueMap.get(profileUsage);
                if (profileUsageValue == null) {
                  out.print(",");
                } else {
                  out.print(makeCsv(profileUsageValue.getValue()));
                }
              }
            }
            out.println();
          }
          {
            out.print(makeCsv(profileUsage.getCategory()));
            out.print(makeCsv(profileUsage.getLabel()));
            out.print(makeCsv(profileUsage.getVersion()));
            out.print(makeCsv("Comments"));
            for (ProfileField profileField : profileManager.getProfileFieldList()) {
              Map<ProfileUsage, ProfileUsageValue> profileUsageValueMap = masterMap.get(profileField);
              if (profileUsageValueMap != null) {
                ProfileUsageValue profileUsageValue = profileUsageValueMap.get(profileUsage);
                if (profileUsageValue == null) {
                  out.print(",");
                } else {
                  out.print(makeCsv(profileUsageValue.getComments()));
                }
              }
            }
            out.println();
          }
          {
            out.print(makeCsv(profileUsage.getCategory()));
            out.print(makeCsv(profileUsage.getLabel()));
            out.print(makeCsv(profileUsage.getVersion()));
            out.print(makeCsv("Notes"));
            for (ProfileField profileField : profileManager.getProfileFieldList()) {
              Map<ProfileUsage, ProfileUsageValue> profileUsageValueMap = masterMap.get(profileField);
              if (profileUsageValueMap != null) {
                ProfileUsageValue profileUsageValue = profileUsageValueMap.get(profileUsage);
                if (profileUsageValue == null) {
                  out.print(",");
                } else {
                  out.print(makeCsv(profileUsageValue.getNotes()));
                }
              }
            }
            out.println();
          }
        }
      } finally {
        out.close();
      }
    }
  }

  private static String makeCsv(String s) {
    return "\"" + s.replace('"', '\'') + "\",";
  }

  private static String makeCsv(Object o) {
    return makeCsv(o.toString());
  }

}
