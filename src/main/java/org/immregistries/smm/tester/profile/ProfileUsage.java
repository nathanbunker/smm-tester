package org.immregistries.smm.tester.profile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProfileUsage {
  private String profileUsageId = "";
  private String label = "";
  private File file = null;

  public String getProfileUsageId() {
    return profileUsageId;
  }
  
  public void setProfileUsageId(String profileUsageId) {
    this.profileUsageId = profileUsageId;
  }
  
  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  private Map<ProfileField, ProfileUsageValue> profileUsageValueMap = new HashMap<ProfileField, ProfileUsageValue>();

  public Map<ProfileField, ProfileUsageValue> getProfileUsageValueMap() {
    return profileUsageValueMap;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ProfileUsage)
    {
      ProfileUsage pu = (ProfileUsage) obj;
      return pu.getProfileUsageId().equals(this.getProfileUsageId());
    }
    return super.equals(obj);
  }

}
