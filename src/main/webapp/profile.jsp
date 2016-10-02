<%-- 
    Document   : profile
    Created on : 02-Oct-2016, 19:59:09
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
            <h1>Your Details</h1>
        <%
            java.util.LinkedList<String> userDetails = (java.util.LinkedList<String>) request.getAttribute("userDetails");
            if (userDetails == null) {
        %>
        <p>No details found</p>
        <%
        } else {
            Iterator<String> iterator;
            iterator = userDetails.iterator();
            while (iterator.hasNext()) {
                String detail = (String) iterator.next();

        %>
        <%=detail%>  <br/><%

            }
            }
        %>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
