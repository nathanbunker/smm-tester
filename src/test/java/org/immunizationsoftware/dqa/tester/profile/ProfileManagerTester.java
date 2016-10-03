package org.immunizationsoftware.dqa.tester.profile;

import static org.junit.Assert.*;
import java.io.IOException;

import org.junit.Test;

public class ProfileManagerTester {

  @Test
  public void test() throws IOException {
    ProfileManager profileManager = new ProfileManager();
    assertTrue(profileManager.getProfileFieldList().size() >= 47103);
    assertTrue(profileManager.getProfileUsageList().size() >= 53);
    boolean foundUsBase = false;
    for (ProfileUsage profileUsage : profileManager.getProfileUsageList()) {
      if (profileUsage.toString().equals("US - Base")) {
        foundUsBase = true;
      }
    }
    assertTrue(foundUsBase);
  }

}
