package org.immunizationsoftware.dqa.transform;

public class ModifyMessageRequest
{
  private String messageOriginal = "";
  private String transformScript = "";
  private String messageFinal = "";

  public String getMessageOriginal() {
    return messageOriginal;
  }

  public void setMessageOriginal(String messageOriginal) {
    this.messageOriginal = messageOriginal;
  }

  public String getTransformScript() {
    return transformScript;
  }

  public void setTransformScript(String transformScript) {
    this.transformScript = transformScript;
  }

  public String getMessageFinal() {
    return messageFinal;
  }

  public void setMessageFinal(String messageFinal) {
    this.messageFinal = messageFinal;
  }
}
