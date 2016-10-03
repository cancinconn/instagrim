<%-- 
    Document   : updateProfile
    Created on : 03-Oct-2016, 00:26:45
    Author     : Can
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>

     <%@ include file="header.jsp" %>
    
    <body>
        
 
        <article>
            <h1>Update Your Details</h1>
        
            <form method="POST"  action="UpdateProfile">
                <ul>
                    <li>First Name <input type="text" name="fname"></li>
                    <li>Last Name <input type="text" name="lname"></li>
                    <li>Confirm your Password to save changes: <input type="password" name="password"></li>
                </ul>
                <br/>
                <input type="submit" value="UpdateProfile"> 
            </form>
        
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
