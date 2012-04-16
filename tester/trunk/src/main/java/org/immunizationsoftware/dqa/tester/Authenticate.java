/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nathan
 */
public class Authenticate {

    public static class User {

        private String password = "";

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
        private String username = "";
        private String name = "";
        private String email = "";

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

    private static void addUser(String username, String password, String name, String email) {
        User u = new User(username, password, name, email);
        map.put(u.username, u);
    }

    public static boolean isValid(String username, String password) {
        init();
        User user = map.get(username);
        return user != null && user.password.equals(password);
    }

    private static void init() {
        if (map == null) {
            map = new HashMap<String, User>();
            addUser("username", "password");
        }
    }
}
