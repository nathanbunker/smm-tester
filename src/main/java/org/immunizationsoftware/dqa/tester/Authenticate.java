/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.util.HashMap;
import java.util.Map;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;

/**
 *
 * @author nathan
 */
public class Authenticate {

    public static class User {

        private String password = "";
        private String username = "";
        private String name = "";
        private String email = "";
        private SendData sendData = null;
        
        public boolean hasSendData()
        {
          return sendData != null;
        }

        public SendData getSendData()
        {
          return sendData;
        }

        public void setSendData(SendData sendData)
        {
          this.sendData = sendData;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public User(String username, String password, String name, String email) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
        }
    }
    private static Map<String, User> map = null;

    public static User getUser(String username) {
        return map.get(username);
    }

    private static void addUser(String username, String password) {
        User u = new User(username, password);
        map.put(u.username, u);
    }

    private static void addUser(String username, String password, SendData sendData) {
      User u = new User(username, password);
      u.setSendData(sendData);
      map.put(u.username, u);
  }

    private static void addUser(String username, String password, String name, String email) {
        User u = new User(username, password, name, email);
        map.put(u.username, u);
    }

    public static boolean isValid(String username, String password) {
        init();
        int passwordInt = 0;
        try 
        {
          passwordInt = Integer.parseInt(password);
        }
        catch (NumberFormatException nfe)
        {
          // not an integer
        }
        SendData sendData = null;
        if (passwordInt > 0)
        {
          sendData = ManagerServlet.authenticateSendData(username, passwordInt);
          addUser(username, password, sendData);
          
          if (sendData != null)
          {
            return true;
          }
        }
        User user = map.get(username);
        return user != null && user.password.equals(password);
    }

    private static void init() {
        if (map == null) {
            map = new HashMap<String, User>();
            
        }
    }

    public static final String APP_DEFAULT_HOME = "HomeServlet";
}
