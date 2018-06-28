package org.immregistries.smm.tester.certify;

import java.util.ArrayList;
import java.util.List;

public class AartUrl {
  private String url = "";
  private List<String> sectionTypeList = new ArrayList<String>();
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public List<String> getSectionTypeList() {
    return sectionTypeList;
  } 
  
  public AartUrl(String url)
  {
    this.url = url;
  }
}
