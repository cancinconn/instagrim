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
        
            
        <!-- Presentation logic for when the user is NOT found -->
        <%
        UserDetails userDetails = (UserDetails) request.getAttribute("userDetails");
        if (userDetails == null) {
        %>
        <p class="pageText">No details found</p>
        <% } else {
        
        //Presentation logic for when the user IS found -->
        %>
        
        <%
            //if this profile belongs to this session's LoggedIn user
            if (lg != null) {
                if (lg.getLoggedIn() && lg.getUsername().equals(userDetails.getUsername())) {
        %>
        
        <h1> <%=userDetails.getUsername()%>'s Profile (Yours!) </h1>
        
        <%      } else { 
        //if this profile belongs to someone else
        %>

        <h1> <%=userDetails.getUsername()%>'s Profile </h1>

        <%
            }
        } else { 
            //if not logged in:
            %>
            <h1> <%=userDetails.getUsername()%>'s Profile </h1>
                    
            <%}%>
            
            
            <!-- Code for formatting page contents: -->
            
            <!-- Display Profile Picture -->
            
            <%
                Pic profilePic = (Pic) request.getAttribute("profilePic");
                if (profilePic == null) {
                %>
                <p class = "pageText">This user does not have a profile picture.</p>
                <%
                } else {
                %>
                <a href="${pageContext.request.contextPath}/Image/<%=profilePic.getSUUID()%>" ><img class = "profilePic" src="${pageContext.request.contextPath}/Thumb/<%=profilePic.getSUUID()%>"></a><br/>
                <%
                }
            %>

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
                
                <div class="buttonDiv">
                    <a href="${pageContext.request.contextPath}/Images/<%=userDetails.getUsername()%>" class="button">View pictures by <%=userDetails.getUsername()%></a>
                </div>
         
         <%
         //Finally, display the profile editing option if this profile belongs to this session's LoggedIn user
         if (lg != null) {
            if (lg.getLoggedIn() && lg.getUsername().equals(userDetails.getUsername())) { %>
                <div class="buttonDiv">
                    <a href="${pageContext.request.contextPath}/UpdateProfile/<%=lg.getUsername()%>" class="button">Update Profile Details</a>
                </div>
        <%  }
        }
        %>
         
        
         <%} //End of page display logic %>
        

        
    </body>
</html>
