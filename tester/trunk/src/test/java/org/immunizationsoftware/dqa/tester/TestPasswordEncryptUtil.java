package org.immunizationsoftware.dqa.tester;

import junit.framework.TestCase;

public class TestPasswordEncryptUtil extends TestCase
{
  public void testEncryptDecrypt() throws Exception
  {
    {
      String plainText = "PASSWORD";
      String encryptedText = PasswordEncryptUtil.encrypt(plainText);
      assertEquals("PASSWORD_HIDDEN_BY_SMM|MGAwEl1TLbQ=", encryptedText);
      String comparePlainText = PasswordEncryptUtil.decrypt(encryptedText);
      assertEquals(plainText, comparePlainText);
    }
    {
      String plainText = "PASSWORD AGAIN";
      String encryptedText = PasswordEncryptUtil.encrypt(plainText);
      assertEquals("PASSWORD_HIDDEN_BY_SMM|MGAwEl1TLbQWg7uGtT2KIw==", encryptedText);
      String comparePlainText = PasswordEncryptUtil.decrypt(encryptedText);
      assertEquals(plainText, comparePlainText);
    }
    {
      String plainText = "PASSWORD didn't work";
      String encryptedText = PasswordEncryptUtil.encrypt(plainText);
      assertEquals("PASSWORD_HIDDEN_BY_SMM|MGAwEl1TLbTx9rAr68Wio6JQt55PGlBY", encryptedText);
      String comparePlainText = PasswordEncryptUtil.decrypt(encryptedText);
      assertEquals(plainText, comparePlainText);
    }
    {
      String plainText = "";
      String encryptedText = PasswordEncryptUtil.encrypt(plainText);
      assertEquals("PASSWORD_HIDDEN_BY_SMM|", encryptedText);
      String comparePlainText = PasswordEncryptUtil.decrypt(encryptedText);
      assertEquals(plainText, comparePlainText);
    }
    System.out.println("--> " + PasswordEncryptUtil.encrypt("I was"));
    System.out.println("--> " + PasswordEncryptUtil.encrypt("xbzL4o7v40IU8I5Y"));
    System.out.println("--> " + PasswordEncryptUtil.encrypt("upload1"));
    System.out.println("--> " + PasswordEncryptUtil.encrypt("470922"));
    System.out.println("--> " + PasswordEncryptUtil.encrypt("Smk37z!9"));
    System.out.println("--> " + PasswordEncryptUtil.encrypt("bzg26ne4lrx317td"));
    System.out.println("--> " + PasswordEncryptUtil.encrypt(""));
    
  }
}
