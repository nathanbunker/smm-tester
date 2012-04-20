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
            addUser("Apple", "ID3S81hs6R70tu8", "Rick Hall", "rphall@comcast.net");
            addUser("Apricot", "3fiBxjoUlr4MF5p", "Caleb Shoemaker", "caleb.shoe@gmail.com");
            addUser("Avocado", "lC26HoJ5P2X3P7c");
            addUser("Banana", "TpN6s7tkSqSA68y");
            addUser("Blackberry", "RI12y21m9a3616g");
            addUser("Breadfruit", "RI12y21m9a3616g");
            addUser("Cherry", "411461FE814r2QN");
            addUser("Coconut", "U7F3Dfu1jt224Wq");
            addUser("Coffee", "Q3Ay6c74KxM6Ch4");
            addUser("Custard", "Ww7xUvc3b574rFu");
            addUser("Date", "bx735h6S6y658rm");
            addUser("Dragon", "Nnk632F0482Qe11");
            addUser("Durian", "YGoCkoH1gI44mCG");
            addUser("Fig", "X7161Vi51RwCp6L");
            addUser("Gooseberry", "1i2E81q2Ff5K66v", "Joel Freborg", "Joel.Freborg@bcbsnd.com");
            addUser("Grape", "5w005eeg707388P");
            addUser("Guava", "72F3NJym543TL6r");
            addUser("Jackfruit", "13vG71f41233S7q");
            addUser("Jujube", "rqK275tcM2Cfi6w");
            addUser("Kiwifruit", "d4UGV51c4POgt1l");
            addUser("Longan", "26Cau26Rd7DcUSb");
            addUser("Lychee", "26Cau26Rd7DcUSb");
            addUser("Mafai", "LU0Z23LJlqW1bQR", "Nathan Bunker", "Nathan.Bunker@gmail.com");
            addUser("Mango", "6Y847F1EL2W55fr", "Tony Mack", "tmack1@health.nyc.gov");
            addUser("Mangosteen", "25O5rk7BB84k9cF", "Angel Aponte", "aaponte@health.nyc.gov");
            addUser("Maprang", "17ExGM5j4CsB765", "Eric Larson", "vev5@cdc.gov");
            addUser("Olive", "y249n5rX60X47Ro", "Paul Groll", "GROLLP@michigan.gov");
            addUser("Orange", "MmUrGMw2B5bS772", "Rod Mach", "rmach@hiperlogic.com");
            addUser("Papaya", "54i627F29GEc21j");
            addUser("Peach", "y3vx68O3i2Eh7bs");
            addUser("Pear", "ZVmKNItmWxNr0U8");
            addUser("Pineapple", "Q1HkUKcCROr2gDN");
            addUser("Plum", "2M7632L8Df011GQ");
            addUser("Pomegranate", "aGWBac39r113uYB");
            addUser("Pomelo", "mR5nOH16HwQSW6e");
            addUser("Rambutan", "m62w2K9D73w9g6z");
            addUser("Raspberry", "A432p3tPo17IcFr");
            addUser("Currant", "Rk1452294qXcv71");
            addUser("Rose", "CqOvK7VKxqHCM4n");
            addUser("Roselle", "7OfpL5930T8h9TY");
            addUser("Santol", "5976g1D16kT1ZE1");
            addUser("Sapodilla", "n9qehfE18WQefxo", "Tom Love", "Cimarronmi@aol.com");
            addUser("Starfruit", "vu756uB60b573L2");
            addUser("Strawberry", "M67981M1wuIj9Tk");
            addUser("Tamarind", "IPkURqU7erHyEgW");
            addUser("Tangerine", "KrFJCE6uO974A80");
            addUser("Watermelon", "M7g1ZG29473v83W");
        }
    }
}
