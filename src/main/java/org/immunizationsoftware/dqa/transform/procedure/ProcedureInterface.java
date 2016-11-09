package org.immunizationsoftware.dqa.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;

import org.immunizationsoftware.dqa.transform.TransformRequest;
import org.immunizationsoftware.dqa.transform.Transformer;

public interface ProcedureInterface
{
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList) throws IOException ;
  
  public void setTransformer(Transformer transformer);
}
