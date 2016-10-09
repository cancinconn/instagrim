<%-- 
    Document   : displayPicture
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
        
            
        <!-- Presentation logic for when the picture is NOT found -->
        <%
        Pic pic = (Pic) request.getAttribute("pic");
        if (pic == null) {
        %>
        <p class="pageText">Image not found.</p>
        <% } else {
        
        //Presentation logic for when the picture IS found -->
        %>
        
        <div class = "centerDivTitleNoBG">
            <p class = "pageText"><%=pic.getTitle()%></p>
        </div>
        <!-- Display pic (output comes from Image servlet, written as an image in the response) -->
        <img class="pic" src="${pageContext.request.contextPath}/RawImage/<%=pic.getSUUID()%>"><br/>
        
        <!-- Display author -->
        <p class = "pageText">by <a href="${pageContext.request.contextPath}/Profile/<%=pic.getUsername()%>"><%=pic.getUsername()%></a></p>
         
        <div class="buttonDiv">
            <a class="button" href="${pageContext.request.contextPath}/Images/<%=pic.getUsername()%>">View other pictures by <%=pic.getUsername()%></a></li>
        </div>
        
        <%
            if (lg != null)
            {
                
        if (pic.getUsername().equals(lg.getUsername()))
        { //If this is a picture that belongs to the user that is currently logged in: %>
        <div class="buttonDiv">
            <a class="button" href="${pageContext.request.contextPath}/UpdateProfilePicture/<%=pic.getSUUID()%>">Set as profile picture</a></li>
        </div>
        
        <%  }
        }%>
        
         <%} //End of page display logic %>
        

        
    </body>
</html>

