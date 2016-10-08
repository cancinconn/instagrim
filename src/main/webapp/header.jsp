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
        
        <div class="leftDiv">
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
            <li><a href="${pageContext.request.contextPath}/Profile/<%=lg.getUsername()%>">Your Profile</a></li>
            
            </div> <!--close leftDiv -->
            <!-- User's Name & related Components go below-->
            
            <div class = "rightDiv">
            <li class="liRight"><a href="${pageContext.request.contextPath}/Profile/<%=lg.getUsername()%>"><%=lg.getUsername()%></a></li>
            <li class="liRight"><a href="${pageContext.request.contextPath}/Logout">Log out</a></li>
            </div>
                <%}
                } else { //ıf not logged in:
                %>
            <li><a href="${pageContext.request.contextPath}/register.jsp">Register</a></li>
            <li><a href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
            </div> <!--close leftDiv -->
                <%
                        }%>
        </ul>
    </div>
</nav>

<!-- TODO: Avoid form resubmission on refresh (eg: after login) -->