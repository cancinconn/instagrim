<%-- 
    Document   : index
    Created on : 13-Sep-2016, 19:25:38
    Author     : Can
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styling.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Instagrim :|</title>
        
        <!-- navigation bar -->
    <nav>

        <ul>


            <li><a href="upload.jsp">Upload</a></li>
                <%

                    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                    if (lg != null) {
                        String UserName = lg.getUsername();
                        if (lg.getlogedin()) { //TODO: Fix spelling
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
    </nav>
        
    </head>
    <body>
        
        <div class="titleBox">
        <p class="barTitle">Instagrim :| </p>
        <p class="barText">Your world in black and white.</p>
        </div>
        
        <div class="feedBox">
        <div class="feedInnerBox"> 
        
        </div>
        <p class="feedCaption"> This is a story. With a picture, too! Lorem ipsum dolor sit amet. </p>
        </div>
        

        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
                <li>&COPY; Andy C</li>
            </ul>
        </footer>
    </body>
    
    
</html>
