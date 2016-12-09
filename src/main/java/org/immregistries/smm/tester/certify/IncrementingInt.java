package org.immregistries.smm.tester.certify;

public class IncrementingInt
{
  private int i = 0;

  public synchronized int next() {
    i++;
    return i;
  }
}
