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
        private boolean isAdmin = false;
        
        public boolean isAdmin()
        {
          return isAdmin;
        }

        public void setAdmin(boolean isAdmin)
        {
          this.isAdmin = isAdmin;
        }

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

    public static void setupAdminUser(String username, String password)
    {
      map = new HashMap<String, User>();
      User u = new User(username, password);
      u.setAdmin(true);
      map.put(u.username, u);
    }
    private static void addUser(String username, String password, String name, String email) {
        User u = new User(username, password, name, email);
        map.put(u.username, u);
    }

    private static void addUser(String username, String password, String name, String email, boolean isAdmin) {
      User u = new User(username, password, name, email);
      u.setAdmin(isAdmin);
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
            addUser("Apple", "ID3S81hs6R70tu8", "Rick Hall", "rphall@comcast.net");
            addUser("Apricot", "3fiBxjoUlr4MF5p", "Caleb Shoemaker", "caleb.shoe@gmail.com");
            addUser("Avocado", "lC26HoJ5P2X3P7c", "Heather Yarde", "hya1@cdc.gov");
            addUser("Banana", "TpN6s7tkSqSA68y", "Jennifer Wain", "jua7@cdc.gov");
            addUser("Blackberry", "RI12y21m9a3616g", "Stuart Myerburg", "jyz0@cdc.gov");
            addUser("Breadfruit", "RI12y21m9a3616g", "Cecile Town", "Cecile.Town@ihs.gov", true);
            addUser("Cherry", "411461FE814r2QN", "Gerry Bragg", "BraggG1@michigan.gov");
            addUser("Coconut", "U7F3Dfu1jt224Wq", "David Willhite", "willhited@michigan.gov");
            addUser("Coffee", "Q3Ay6c74KxM6Ch4", "Kevin Samuelson", "kevin.samuelson@hp.com");
            addUser("Custard", "Ww7xUvc3b574rFu", "Sichi Inturi", "InturiS@michigan.gov");
            addUser("Date", "bx735h6S6y658rm", "Therese Hoyle", "therese.hoyle@gmail.com");
            addUser("Dragon", "Nnk632F0482Qe11", "Steve Murchie", "smurchie@envisiontechnology.com");
            addUser("Durian", "YGoCkoH1gI44mCG", "Ian Hancke", "HanckeI@michigan.gov");
            addUser("Fig", "X7161Vi51RwCp6L", "Kevin Snow", "ksnow@envisiontechnology.com");
            addUser("Gooseberry", "1i2E81q2Ff5K66v", "Joel Freborg", "Joel.Freborg@bcbsnd.com");
            addUser("Grape", "5w005eeg707388P", "Pam Kowalske", "pkowalsk@mphi.org");
            addUser("Guava", "72F3NJym543TL6r", "Karen McGettigan", "kmcgetti@mphi.org");
            // http://ois-pt.org/tester/LoginServlet?username=Jujube&password=rqK275tcM2Cfi6w&action=Login
            // http://ois-pt.org/tester/LoginServlet?username=Kiwifruit&password=d4UGV51c4POgt1l&action=Login
            // http://ois-pt.org/tester/LoginServlet?username=Maprang&password=17ExGM5j4CsB765&action=Login
            addUser("Jackfruit", "13vG71f41233S7q");
            addUser("Jujube", "rqK275tcM2Cfi6w", "Kevin Murset", "kevinmurset@gmail.com", true);
            addUser("Kiwifruit", "d4UGV51c4POgt1l", "Nichole Lambrecht", "nlambrecht@immregistries.org", true);
            addUser("Longan", "26Cau26Rd7DcUSb");
            addUser("Lychee", "26Cau26Rd7DcUSb");
            addUser("Mafai", "LU0Z23LJlqW1bQR", "Nathan Bunker", "Nathan.Bunker@gmail.com", true);
            addUser("Mango", "6Y847F1EL2W55fr", "Tony Mack", "tmack1@health.nyc.gov");
            addUser("Mangosteen", "25O5rk7BB84k9cF", "Angel Aponte", "aaponte@health.nyc.gov");
            addUser("Maprang", "17ExGM5j4CsB765", "Eric Larson", "elarson@immregistries.org ", true);
            addUser("Olive", "y249n5rX60X47Ro", "Paul Groll", "GROLLP@michigan.gov");
            addUser("Orange", "MmUrGMw2B5bS772", "Rod Mach", "rmach@hiperlogic.com");
            addUser("Papaya", "54i627F29GEc21j");
            addUser("Peach", "y3vx68O3i2Eh7bs");
            addUser("Pear", "ZVmKNItmWxNr0U8");
            addUser("Pineapple", "Q1HkUKcCROr2gDN");
            addUser("Plum", "2M7632L8Df011GQ", "Rob Savage", "hzv3@cdc.gov");
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

    public static final String APP_DEFAULT_HOME = "HomeServlet";
}
