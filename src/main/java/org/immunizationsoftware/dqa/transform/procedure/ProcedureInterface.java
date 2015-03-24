package org.immunizationsoftware.dqa.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;

import org.immunizationsoftware.dqa.transform.TransformRequest;

public interface ProcedureInterface
{
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList) throws IOException ;
}
