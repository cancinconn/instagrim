<%-- 
    Document   : header
    Created on : 25-Sep-2016, 20:57:24
    Author     : Can
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>

<%boolean isLoggedIn = false; //For use in jsp views. Initially assumed false, changed later.%>

<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styling.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome to Instagrim :|</title>

    <!-- navigation bar -->

<nav>

    <div class="titleBox">
        
        <div class="leftDiv">
        
            <p class="barTitle"><a href="${pageContext.request.contextPath}">Instagrim :| </a></p>
            <p class="barText"><a href="${pageContext.request.contextPath}">Your world in black and white.</a></p>
        
        <ul>
            <li><a href="${pageContext.request.contextPath}/Upload">Upload</a></li>
                <%
                    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                    if (lg != null) {
                        String UserName = lg.getUsername();
                        if (lg.getLoggedIn()) {
                            isLoggedIn = true;
                %>

            <li><a href="${pageContext.request.contextPath}/Images/<%=lg.getUsername()%>">Your Images</a></li>
            <li><a href="${pageContext.request.contextPath}/Feed">Your Feed</a></li>
            
            </div> <!--close leftDiv -->
            <!-- User's Name & related Components go below-->
            
            <div class = "rightDiv">
            <li class="liRight"><a href="${pageContext.request.contextPath}/Profile/<%=lg.getUsername()%>">Logged in as <%=lg.getUsername()%></a></li>
            <li class="liRight"><a href="${pageContext.request.contextPath}/Logout">Log out</a></li>
            </div>
                <%}
                } else { //ıf not logged in:
                %>
            <li><a href="${pageContext.request.contextPath}/Register">Register</a></li>
            <li><a href="${pageContext.request.contextPath}/Login">Login</a></li>
            </div> <!--close leftDiv -->
                <%
                        }%>
        </ul>
    </div>
</nav>

</head>

<!-- Show any messages for the user -->
<%@ include file="notifications.jsp" %>