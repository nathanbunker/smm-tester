package org.immunizationsoftware.dqa.tester;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CatchServlet extends ClientServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    System.out.println("POST called");
    dumpDetails(req);
    printForm(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("GET called");

    dumpDetails(req);
    resp.setContentType("text/html;charset=UTF-8");

    printForm(req, resp);
  }

  private void printForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, req);
      out.println("<form action=\"CatchServlet\" method=\"POST\">");
      out.println("  <input type=\"text\" name=\"name\" value=\"\"/>");
      out.println("  <input type=\"submit\" name=\"submit\" value=\"Submit\"/>");
      out.println("</form>");
      printHtmlFoot(out);
    }
    finally
    {
      out.close();
    }
  }

  private void dumpDetails(HttpServletRequest req) throws IOException {
    
    System.out.println("Path: " + req.getPathInfo());
    System.out.println("Query: " + req.getQueryString());
    System.out.println("Protocol: " + req.getProtocol());
    System.out.println("URI: " + req.getRequestURI());
    System.out.println("Header Params: ");
    Enumeration<?> headerNames = req.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      Object headerName = headerNames.nextElement();
      System.out.println(headerName + ": " + req.getHeader(headerName.toString()));
    }

    if (req.getContentLength() > 0) {
      BufferedInputStream in = new BufferedInputStream(req.getInputStream());
      int b;
      while ((b = in.read()) != -1) {
        System.out.print((char) b);
      }
      in.close();
    }
  }
}
