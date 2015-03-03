/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

/**
 *
 * @author nathan
 */
public class DownloadServlet extends HttpServlet {

    private static int count = 0;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
        } else {

            String action = request.getParameter("action");
            response.setContentType("text/html;charset=UTF-8");
            count++;
            String filename = "TestHL7-" + count + ".txt";
            if (action != null && action.equals("Download Script")) {
                filename = "TestHL7Script-" + count + ".txt";
            }
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            PrintWriter out = response.getWriter();
            try {
                String testScript = request.getParameter("testScript");
                List<TestCaseMessage> testCaseMessageList;
                if (testScript != null) {
                    testCaseMessageList = TestCaseServlet.parseAndAddTestCases(testScript, session);
                } else {
                    testCaseMessageList = (List<TestCaseMessage>) session.getAttribute("selectedTestCaseMessageList");
                }
                TestCaseServlet.sortTestCaseMessageList(testCaseMessageList);
                if (action != null) {
                    if (action.equals("Download Script")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        out.println("-- HL7 Script downloaded " + sdf.format(new Date()) + "");
                        Authenticate.User user = Authenticate.getUser(username);
                        if (!user.getName().equals("")) {
                            out.println("-- User: " + user.getName() + " (" + user.getUsername() + ")");
                        } else {
                            out.println("-- User: " + user.getName());
                        }
                        out.println();
                        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
                            out.println(testCaseMessage.createText());
                            out.println();
                        }
                    } else if (action.equals("Download HL7 Only")) {
                        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
                            out.print(testCaseMessage.getMessageText());
                        }
                    } else {
                        out.println("Unrecognized action");
                    }
                } else {
                    out.println("Action not specified");
                }

            } finally {
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
