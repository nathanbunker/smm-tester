package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;

import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public interface ProcedureInterface {
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException;

  public void setTransformer(Transformer transformer);
}
