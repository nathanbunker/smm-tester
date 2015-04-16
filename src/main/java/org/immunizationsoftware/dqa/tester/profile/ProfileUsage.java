package org.immunizationsoftware.dqa.tester.profile;

import java.util.HashMap;
import java.util.Map;

public class ProfileUsage
{
  private ProfileCategory category = null;
  private String label = "";
  private String version = "";
  
  private Map<ProfileField, ProfileUsageValue> profileUsageValueMap = new HashMap<ProfileField, ProfileUsageValue>(); 
  
  public Map<ProfileField, ProfileUsageValue> getProfileUsageValueMap() {
    return profileUsageValueMap;
  }

  public ProfileCategory getCategory() {
    return category;
  }

  public void setCategory(ProfileCategory category) {
    this.category = category;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
  
  @Override
  public String toString() {
    if (version.equals("")) {
      return category + " - " + label;
    }
    return category + " - " + label + " - " + version;
  }


}
