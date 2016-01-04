package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_FULL_RECORD_FOR_PROFILING;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.profile.MessageAcceptStatus;
import org.immunizationsoftware.dqa.tester.profile.ProfileLine;
import org.immunizationsoftware.dqa.tester.profile.ProfileManager;
import org.immunizationsoftware.dqa.tester.profile.Usage;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.tester.transform.Issue;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAProfiling extends CertifyArea
{

  public CAProfiling(CertifyRunner certifyRunner) {
    super("I", VALUE_TEST_SECTION_TYPE_PROFILING, certifyRunner);
  }

  private TestCaseMessage tcmFull = null;
  private ProfileManager profileManager = null;
  private List<ProfileLine> profileLineList = null;

  public void setTcmFull(TestCaseMessage tcmFull) {
    this.tcmFull = tcmFull;
  }

  public TestCaseMessage getTcmFull() {
    return tcmFull;
  }

  public List<ProfileLine> getProfileLineList() {
    return profileLineList;
  }

  public void setProfileLineList(List<ProfileLine> profileLineList) {
    this.profileLineList = profileLineList;
  }

  public ProfileManager getProfileManager() {
    return profileManager;
  }

  public void setProfileManager(ProfileManager profileManager) {
    this.profileManager = profileManager;
  }

  @Override
  public void prepareUpdates() {
    if (profileManager == null) {
      logStatus("Profiling not setup");
      return;
    }
    if (certifyRunner.profileUsage == null) {
      logStatus("Profile not selected");
      return;
    }

    tcmFull = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
    tcmFull.setDescription("Base Record");
    tcmFull.setHasIssue(true);
    register(0, tcmFull).setTestType(VALUE_TEST_TYPE_PREP);

    certifyRunner.logStatus("Running full test record to see it will be accepted");
    runUpdate(tcmFull);

    if (tcmFull.isAccepted()) {
      logStatus("Full record for profiling was accepted, profiling of all fields can begin");
    } else {
      logStatus("Full record for profiling was NOT accepted, profiling of all fields can not be conducted");
    }
    certifyRunner.reportProgress(tcmFull);
    incrementUpdateProgress();

    if (tcmFull.isAccepted()) {
      profileLineList = profileManager.createProfileLines(certifyRunner.profileUsage, false);
      ProfileManager.updateMessageAcceptStatus(profileLineList);
      logStatus("Found " + profileLineList.size() + " of profile lines to test");
      addProfilingTestCases(profileLineList);
    } else {
      areaScore[0] = 0;
      areaProgress[0] = 100;
      reportProgress(null);
    }
  }

  private void addProfilingTestCases(List<ProfileLine> profileLineList) {
    int count = 0;
    for (ProfileLine profileLine : profileLineList) {
      profileLine.setUsageDetected(profileLine.getUsage());
      count++;
      TestCaseMessage testCaseMessagePresent = ProfileManager.getPresentTestCase(profileLine.getField(), tcmFull);
      TestCaseMessage testCaseMessageAbsent = ProfileManager.getAbsentTestCase(profileLine.getField(), tcmFull);
      profileLine.setTestCaseMessagePresent(testCaseMessagePresent);
      profileLine.setTestCaseMessageAbsent(testCaseMessageAbsent);
      if (testCaseMessagePresent.hasIssue() && testCaseMessageAbsent.hasIssue()) {
        if (testCaseMessagePresent != tcmFull) {
          testCaseMessagePresent.setDescription("Field " + profileLine.getField().getFieldName() + " is present");
          register(count, 1, testCaseMessagePresent);
          if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
            testCaseMessagePresent.setAssertResult("Accept");
          } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
            testCaseMessagePresent.setAssertResult("Reject");
          } else {
            testCaseMessagePresent.setAssertResult("Accept");
          }
          testCaseMessagePresent.setMessageAcceptStatusDebug(profileLine.getMessageAcceptStatusDebug());
        }
        if (testCaseMessageAbsent != tcmFull) {
          testCaseMessageAbsent.setDescription("Field " + profileLine.getField().getFieldName() + " is absent");
          register(count, 1, testCaseMessageAbsent);
          if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
            testCaseMessageAbsent.setAssertResult("Reject");
          } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
            testCaseMessageAbsent.setAssertResult("Accept");
          } else {
            testCaseMessageAbsent.setAssertResult("Accept");
          }
          testCaseMessageAbsent.setMessageAcceptStatusDebug(profileLine.getMessageAcceptStatusDebug());
        }
      }
    }
  }

  @Override
  public void sendUpdates() {
    updateProfiling(profileLineList);
  }

  private void updateProfiling(List<ProfileLine> profileLineList) {
    if (profileLineList != null) {
      TestRunner testRunner = createTestRunner();
      int countPass = 0;
      int countTested = 0;
      Map<String, TestCaseMessage> testCaseMessageMap = new HashMap<String, TestCaseMessage>();
      for (ProfileLine profileLine : profileLineList) {
        profileLine.setUsageDetected(profileLine.getUsage());
        TestCaseMessage testCaseMessagePresent = profileLine.getTestCaseMessagePresent();
        TestCaseMessage testCaseMessageAbsent = profileLine.getTestCaseMessageAbsent();
        try {
          if (testCaseMessagePresent.hasIssue() && testCaseMessageAbsent.hasIssue()) {
            if (testCaseMessagePresent != tcmFull) {
              testCaseMessagePresent = testRunner.runTestIfNew(certifyRunner.connector, testCaseMessagePresent,
                  testCaseMessageMap);
              if (testRunner.isWasRun()) {
                certifyRunner.performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessagePresent);
                incrementUpdateProgress();
              }
            }
            profileLine.setTestCaseMessageAbsent(testCaseMessageAbsent);
            if (testCaseMessageAbsent != tcmFull) {
              testRunner.runTestIfNew(certifyRunner.connector, testCaseMessageAbsent, testCaseMessageMap);
              if (testRunner.isWasRun()) {
                certifyRunner.performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessageAbsent);
                incrementUpdateProgress();
              }
            }
            if (testCaseMessagePresent.isAccepted() && !testCaseMessageAbsent.isAccepted()) {
              profileLine.setMessageAcceptStatusDetected(MessageAcceptStatus.ONLY_IF_PRESENT);
            } else if (!testCaseMessagePresent.isAccepted() && testCaseMessageAbsent.isAccepted()) {
              profileLine.setMessageAcceptStatusDetected(MessageAcceptStatus.ONLY_IF_ABSENT);
            } else {
              profileLine.setMessageAcceptStatusDetected(MessageAcceptStatus.IF_PRESENT_OR_ABSENT);
            }
            if (profileLine.getMessageAcceptStatusDetected() != profileLine.getMessageAcceptStatus()) {
              if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
                if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.IF_PRESENT_OR_ABSENT) {
                  profileLine.setUsageDetected(Usage.R_NOT_ENFORCED);
                } else if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_ABSENT) {
                  profileLine.setUsageDetected(Usage.X);
                }
              } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.IF_PRESENT_OR_ABSENT) {
                if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_PRESENT) {
                  if (profileLine.getUsage() == Usage.R) {
                    profileLine.setUsageDetected(Usage.R_SPECIAL);
                  } else {
                    profileLine.setUsageDetected(Usage.R);
                  }
                } else if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_ABSENT) {
                  profileLine.setUsageDetected(Usage.X);
                }
              } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
                if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_PRESENT) {
                  if (profileLine.getUsage() == Usage.R) {
                    profileLine.setUsageDetected(Usage.R_SPECIAL);
                  } else {
                    profileLine.setUsageDetected(Usage.R);
                  }
                } else if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.IF_PRESENT_OR_ABSENT) {
                  profileLine.setUsageDetected(Usage.X_NOT_ENFORCED);
                }
              }

            }
            if (testCaseMessagePresent.isPassedTest() && testCaseMessageAbsent.isPassedTest()) {
              countPass++;
              profileLine.setPassed(true);
            }
            countTested++;
            profileLine.setHasRun(true);
            certifyRunner.printExampleMessage(testCaseMessagePresent, "I Profiling");
            certifyRunner.printExampleMessage(testCaseMessageAbsent, "I Profiling");
          }
        } catch (Throwable t) {
          testCaseMessagePresent.setException(t);
        }
        testCaseMessagePresent.setHasRun(true);
        if (profileLine.isHasRun()) {
          certifyRunner.saveTestCase(testCaseMessagePresent);
          certifyRunner.reportProgress(testCaseMessagePresent, false, profileLine);
        }
        testCaseMessageAbsent.setHasRun(true);
        if (profileLine.isHasRun()) {
          certifyRunner.saveTestCase(testCaseMessageAbsent);
          certifyRunner.reportProgress(testCaseMessageAbsent, false, profileLine);
        }
        if (!certifyRunner.keepRunning) {
          certifyRunner.status = certifyRunner.STATUS_STOPPED;
          reportProgress(null);
          return;
        }
      }
      if (countTested > 0) {
        areaScore[0] = makeScore(countPass, countTested);
      }
      areaProgress[0] = 100;
      reportProgress(null);
    }
  }

  @Override
  public void prepareQueries() {
    doPrepareQueries();
  }

  @Override
  public void sendQueries() {
    runQueries();
  }
}
