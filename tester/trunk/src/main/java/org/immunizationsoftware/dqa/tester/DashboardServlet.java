/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author nathan
 */
public class DashboardServlet extends ClientServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static String currentYear;

  static {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    currentYear = "" + year;
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

  /**
   * Handles the HTTP <code>GET</code> method.
   * 
   * @param request
   *          servlet request
   * @param response
   *          servlet response
   * @throws ServletException
   *           if a servlet-specific error occurs
   * @throws IOException
   *           if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    try {
      printHtmlHead(out, MENU_HEADER_TEST, request);
      
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }


}
