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
        
 
            <!--TODO: Change output depending on whether it is the profile of the logged-in user or someone else's)  -->
            
        <%
            UserDetails userDetails = (UserDetails) request.getAttribute("userDetails");
    if (userDetails == null) {
        %>
        <p>No details found</p>
        <% } else { %>
        
                <%
                    //if this profile belongs to this session's LoggedIn user
            if (lg != null) {
                if (lg.getLoggedIn() && lg.getUsername().equals(userDetails.getUsername())) {
        %>
        
        <h1> <%=userDetails.getUsername()%>'s Profile (Yours!) </h1>
        
                <%      }
else {
%>

        <h1> <%=userDetails.getUsername()%>'s Profile </h1>

<%
}

            } else { //if not logged in:
%>
            <h1> <%=userDetails.getUsername()%>'s Profile </h1>
                    
            <%}%>

            <!-- 3 divs: center div to contain left and right divs. left identifies the data, right displays it. -->
            
            <div class="centerDiv"> 
            
            <div class="leftDivProfile"> 
            
            <p class="pRight">First Name: </p>
            <p class="pRight"> Last Name: </p>
            
            </div>
            
            <div class="rightDivProfile"> 
            
                <% if (userDetails.getFirstName() != null) { %> <p class="pLeft"><%=userDetails.getFirstName()%> </p>  <% } else { %> <p class="pLeft"> Unknown </p> <% } %>
                <% if (userDetails.getFirstName() != null) { %> <p class="pLeft"><%=userDetails.getLastName()%> </p>  <% } else { %> <p class="pLeft"> Unknown </p> <% } %>
            
            </div>
            
            </div>
            

         
         <br/>
        
         <% } %>
        
        <%
            //if this profile belongs to this session's LoggedIn user
            if (lg != null) {
                if (lg.getLoggedIn() && lg.getUsername().equals(userDetails.getUsername())) {
        %>
        
        <li class="footer"><a href="${pageContext.request.contextPath}/UpdateProfile/<%=lg.getUsername()%>">Update Profile Details</a></li>
        
        <%      }
            }%>
        
    </body>
</html>
