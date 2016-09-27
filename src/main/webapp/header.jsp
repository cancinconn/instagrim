<%-- 
    Document   : header
    Created on : 25-Sep-2016, 20:57:24
    Author     : Can
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>

<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styling.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome to Instagrim :|</title>

    <!-- navigation bar -->

<nav>

    <div class="titleBox">
        <a href="${pageContext.request.contextPath}" >
            <p class="barTitle">Instagrim :|  </p>
            <p class="barText">Your world in black and white.</p>
        </a>
        <ul>
            <li><a href="upload.jsp">Upload</a></li>
                <%

                    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                    if (lg != null) {
                        String UserName = lg.getUsername();
                        if (lg.getLoggedIn()) {
                %>

            <li><a href="${pageContext.request.contextPath}/Images/<%=lg.getUsername()%>">Your Images</a></li>
                <%}
                } else {
                %>
            <li><a href="${pageContext.request.contextPath}/register.jsp">Register</a></li>
            <li><a href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
                <%
                        }%>
        </ul>
    </div>
</nav>
