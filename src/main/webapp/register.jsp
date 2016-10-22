<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
     <%@ include file="header.jsp" %>
    
    <body>
        <header>
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
       
        <article>
            <h3>Register as user</h3>
            <form method="POST"  action="Register">
                <ul>
                    <li>User Name <input type="text" name="username" pattern=".{3,}" required title="Must be at least 3 characters, only alphanumeric characters and underscores are allowed."> (3 or more characters)</li>
                    <li>Password <input type="password" name="password" pattern=".{8,}" required title="Must be at least 8 characters, only alphanumeric characters and underscores are allowed."> (8 or more characters)</li>
                    <li>First Name <input type="text" name="fname" required> (required)</li>
                    <li>Last Name <input type="text" name="lname"></li>
                </ul>
                <br/>
                <input type="submit" value="Register"> 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="${pageContext.request.contextPath}">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
