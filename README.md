# smm-tester
*The American Immunization Registry Association (AIRA) supports the Simple Message Mover / Tester (SMM/Tester) to support connections and testing with Immunization Information Systems (IIS).* 

## Installation Instructions for Windows Environments:
- Create the following folder:
  + `C:\smm`
- Inside the `smm` folder we will place the `smm.config.txt` file that will be configured later

### Software Needed:

#### 1. Java JDK

Ensure that you have Java SE version 6.0 (also displayed as 1.6) or later installed:

- To confirm the version of Java installed on your system, follow these steps:
  + Open a command window (in Windows click Start >> Run and type `cmd`, press enter)
  + Type `java -version` on the command line
  + Verify the version number displayed (remember that 1.6 indicates Java version 6.0, and 1.7 is Java 7.0)

**If Java is not currently installed:**

- Java SE Development Kit (JDK) version 7: [available here](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)

#### 2. Tomcat

- Tomcat 7.0 or newer (NOTE: Please use the "Windows Service Installer): [available here](https://tomcat.apache.org/download-70.cgi)
  + Setup instructions: [available here](http://tomcat.apache.org/tomcat-7.0-doc/setup.html)
  + Follow environment variable section here: [link](https://www.ntu.edu.sg/home/ehchua/programming/howto/JDK_HowTo.html#Set-JAVA_HOME)

##### To start Tomcat:
###### Method 1:
- Open `cmd` prompt
  + `cd` to `C:\*tomcat installation location*\bin`, type `startup` and press Enter

###### Method 2:
- Run .bat file:
  + `C:\*tomcat installation location*\bin>startup.bat`

##### Download smm.war file
- Download `smm.war` here : [link](https://app.immregistries.org/tester/install/index.html)
  + Place `smm.war` in `C:\*tomcat installation location*\webapps` folder

##### smm.config.txt configuration
The configuration of an SMM connection to an IIS is defined by a configuration file named `smm.config.txt` located in the IIS Transfer Folder. This file is key to SMM operation as the SMM will not send data unless this file is present.

The format of this file is both human and SMM readable. Changes can be made by the SMM user, but normally this file is created in the installation process and is not changed during operation. Here is a a quick explanation of some of the common configuration items:

- **Label:** This is a short label or description for this interface that makes sense to the SMM user.
- **Type:** Indicates what basic type of transport is to be used.
- **URL:** Indicates the address of the IIS where the data should be sent.
- **User Id:** Value assigned by IIS.
- **Password:** Value assigned by IIS. Can be entered as plain text but if generated during installation the password will be hidden in an encrypted string.
- **Facility Id:** Value assigned by IIS.

##### Accessing SMM
- If Tomcat has been installed on the default port `8080` then you can login by going to: [http://localhost:8080/smm/](http://localhost:8080/smm/)

##### Access Credentials:
`//todo`
