package org.immregistries.smm.mover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class TenIISConverter {

  private static final char CR = 13;
  private static final char LF = 10;

  public static void main(String[] args) throws Exception {
    String filenameIn = args[0];
    String filenameOut = args[1];
    PrintWriter out = new PrintWriter(new File(filenameOut));
    BufferedReader in = new BufferedReader(new FileReader(new File(filenameIn)));
    String line;
    boolean mshFoundOnce = false;
    int count = 0;
    while ((line = in.readLine()) != null) {
      if (line.startsWith("BTS") || (mshFoundOnce && line.startsWith("MSH"))) {
        out.print(LF);
      }
      out.print(line);
      out.print(CR);
      if (line.startsWith("FHS") || line.startsWith("BHS") || line.startsWith("BTS") || line.startsWith("FTS")) {
        out.print(LF);
      }
      if (line.startsWith("MSH")) {
        mshFoundOnce = true;
        count++;
      }
    }
    out.close();
    System.out.println("Done, processed " + count + " message" + (count == 1 ? "" : "s"));
  }
}
