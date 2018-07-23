package org.immregistries.smm.tester;

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
    
  }
}
