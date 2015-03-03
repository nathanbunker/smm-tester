/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

/**
 * 
 * @author nathan
 */
public abstract class IssueCreator
{

  // Tests to write
  // CPT Admin
  // CVX Admin
  // CVX Historical
  // VFC Admin
  // Vaccine Admin
  public static String createTransforms(TestCaseMessage testCaseMessage)
  {
    String transforms = "";
    BufferedReader inTransform = new BufferedReader(new StringReader(testCaseMessage.getCauseIssues()));
    String line;
    try
    {
      while ((line = inTransform.readLine()) != null)
      {
        line = line.trim();
        boolean not = false;
        if (line.startsWith("NOT "))
        {
          not = true;
          line = line.substring(4).trim();
        }
        Issue issue = null;
        for (Issue i : Issue.values())
        {
          if (line.equalsIgnoreCase(i.getName()))
          {
            issue = i;
            break;
          }
        }
        if (issue != null)
        {
          transforms = IssueCreatorHeader.createTransforms(testCaseMessage, issue, transforms, not);
          transforms = IssueCreatorNextOfKin.createTransforms(testCaseMessage, issue, transforms, not);
          transforms = IssueCreatorObservation.createTransforms(testCaseMessage, issue, transforms, not);
          transforms = IssueCreatorPatient.createTransforms(testCaseMessage, issue, transforms, not);
          transforms = IssueCreatorVaccination.createTransforms(testCaseMessage, issue, transforms, not);
        }
      }
    } catch (IOException ioe)
    {
      throw new IllegalArgumentException("Error reading string", ioe);
    }
    return transforms;
  }
}
