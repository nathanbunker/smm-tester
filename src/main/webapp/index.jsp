<%-- 
    Document   : index
    Created on : Jul 27, 2011, 3:15:24 PM
    Author     : eric
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>OIS Immunization Messaging Client</title>
        <link rel="stylesheet" type="text/css" href="index.css" />
    </head>
    <body>
        <%=org.immunizationsoftware.dqa.tester.ClientServlet.makeMenu(request, "Home")%>
        <h1>OIS Immunization Registry Tester</h1>
        <p>
            This application was originally built to simply test the
            latest SOAP standard that was developed by the CDC convened
            Transport Layer Workgroup. After that demonstration project was
            successfully completed work was done to add simple support
            for submitting test messages and verifying the results. Then
            improvements were made for creating test case scripts. This final
            version incorporates all of this into a single integrated
            test environment. In addition, this application has become
            effective enough it now requires a login. If you need credentials
            please contact Nathan Bunker.
        </p>
        <h2>Support Functions</h2>
        <table width="700" border="1" cellspacing="0" cellpadding="5">
            <tr>
                <td nowrap><a href="SetupServlet">Setup</a></td>
                <td>
                    This application does not store any data permanently.
                    All information entered or displayed will be discarded 
                    if log out or when the page remains inactive for a certain
                    period of time. The setup interface allows for quick 
                    configuration of both connections and test case scripts.
                    For ease of use, it is recommended that scripts should
                    be saved at the end of each session for easy continuation
                    the next time.
                </td>
            </tr>
            <tr>
                <td nowrap><a href="CreateTestCaseServlet">Edit</a></td>
                <td>
                    Quickly create test case scripts that can be used
                    to test immunization registries. You can start with your
                    own HL7 messages or use the default base supplied.
                    Quickly modify the message by indicating automatic and
                    custom changes you want made to it. The resulting
                    script can be saved or run in the tester.
                </td>
            </tr>
            <tr>
                <td nowrap><a href="SubmitServlet">Send Message</a></td>
                <td>
                    Test an immunization registry interface to confirm
                    that ability to connect. Supports latest SOAP standard
                    and HTTPS POST standards. The message you send is returned
                    with the acknowledgement from the connecting system.
                </td>
            </tr>
            <tr>
                <td nowrap><a href="testCase">Run Tests</a></td>
                <td>
                    Test an immunization interface for
                    conformance to data quality standards. 
                </td>
            </tr>
        </table>
        <h2>Example Test Scripts</h2>
        <p>
            Test scripts are the heart of this application, they define
            the message to send and the response expected from the registry.
            In addition, they can be used to create or transform HL7 messages
            or create hundreds of example messages.
        </p>
        <ul>
            <li><a href="tests/CDC MIROW 3 Tests.txt">CDC MIROW 3 Tests.txt</a></li>
            <li><a href="tests/MCIR MG Tests.txt">MCIR MG Tests.txt</a></li>
            <li><a href="tests/ImmTrac Basic 1.txt">ImmTrac Basic 1.txt</a></li>
        </ul>
        <p>Open Immunization Software - Immunization Registry Tester 2011 - Version 1.2<br>
            For questions or support please contact <a href="http://nathanbunker.com/">Nathan Bunker</a>.</p>
    </body>
</html>
