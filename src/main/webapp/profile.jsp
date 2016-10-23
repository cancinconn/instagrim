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
                
                <br>

            <!-- 3 divs: center div to contain left and right divs. left identifies the data, right displays it. -->
            
            <div class="centerDiv"> 
                <div class="leftDivProfile"> 
                    <p class="pRight">First Name: </p>
                    <p class="pRight"> Last Name: </p>
                    <p class="pRight"> E-mail Address: </p>
                </div>
                <div class="rightDivProfile2"> 
                    <% if (userDetails.getFirstName() != null) { %> <p class="pLeft"><%=userDetails.getFirstName()%> </p>  <% } else { %> <p class="pLeft"> Unknown </p> <% } %>
                    <% if (userDetails.getLastName() != null) { %> <p class="pLeft"><%=userDetails.getLastName()%> </p>  <% } else { %> <p class="pLeft"> Unknown </p> <% } %>
                     <% if (!userDetails.getIsEmailPrivate()) { %> <p class="pLeft"><%=userDetails.getEmail()%> </p>  <% } else { %> <p class="pLeft"> Unknown </p> <% } %>
                </div>
            </div>
                
                <div class="buttonDiv">
                    <a href="${pageContext.request.contextPath}/Images/<%=userDetails.getUsername()%>" class="button">View pictures by <%=userDetails.getUsername()%></a>
                </div>
         
         <%
         //Display the profile editing option if this profile belongs to this session's LoggedIn user
         if (lg != null) {
            if (lg.getLoggedIn() && lg.getUsername().equals(userDetails.getUsername())) { %>
                <div class="buttonDiv">
                    <a href="${pageContext.request.contextPath}/UpdateProfile/<%=lg.getUsername()%>" class="button">Update Profile Details</a>
                </div>
        <%  }
            else if (lg.getLoggedIn() && !lg.getUsername().equals(userDetails.getUsername()))
            {
                //Display Follow section if we are logged in but this is not us.
                %>
                
                
                <% boolean isFollowing = (boolean)request.getAttribute("isFollowing");
                if (!isFollowing)
                {%>
                
                <div class="buttonDiv">
                    <a href="${pageContext.request.contextPath}/Follow/<%=userDetails.getUsername()%>" class="button">Follow <%=userDetails.getUsername()%></a>
                </div>
                
                <%} else { //we are following the user already:%>
                
                <div class="buttonDiv">
                    <p class="pageText">You follow <%=userDetails.getUsername()%>.</p>
                </div>
                
                <div class="buttonDiv">
                    <a href="${pageContext.request.contextPath}/Unfollow/<%=userDetails.getUsername()%>" class="button">Unfollow <%=userDetails.getUsername()%></a>
                </div>
                
                <% }%>
                
                    
                <%
            }
        }
        %>
        
        <% //Display follows
            
            LinkedList<Following> followList = (LinkedList<Following>) request.getAttribute("follows");
            
            if (!followList.isEmpty())
            {
            %>
                <p class="pageText">Users that <%=userDetails.getUsername()%> follows:</p>
                
                <table align="center">
                
                <% int counter=0;
                    int rowCount = 3;
                    for (Following follow : followList) 
                {%>
                <!-- PRESENTING FOLLOWED USER -->
                
                <!-- Put a new row in every time counter is divisible by rowCount. -->
                        <% if ((counter % rowCount) == 0)
                        { %>
                        </tr> <!--end the last table row-->
                        <tr> <!-- start a new table row-->
                        <%
                        } %>

                     <!-- display the image -->
                    <td>
 
                        <div class="commentLeftDiv">
                            <!-- profile picture -->
                            <div class="commentPicDiv">

                                <% if(follow.getProfilePictureSUUID() == null) 
                                {%>
                                    <a class = "blankCommentPic" href="${pageContext.request.contextPath}/Profile/<%=follow.getUsername()%>" ></a>
                                <%} else {%>
                                    <a href="${pageContext.request.contextPath}/Profile/<%=follow.getUsername()%>" ><img class = "commentPic" src="${pageContext.request.contextPath}/Thumb/<%=follow.getProfilePictureSUUID()%>"></a>
                                <%}%>
                            </div>
                            <!-- comment author -->
                            <p class = "pageText"><a href="${pageContext.request.contextPath}/Profile/<%=follow.getUsername()%>"><%=follow.getUsername()%></a></p>
                        </div>
                            
                    </td>
                    
                <%counter++;%>
                
                <%}%>
                
                </tr> <!-- End the last row -->
                
                </table>
            
            <% } //end of "if followList is not empty"
            else {%>

                <p class="pageText"><%=userDetails.getUsername()%> does not follow anyone. It must be lonely for them! :|</p>
            
            <% } %>
         
        
         <%} //End of page display logic %>
        

        
    </body>
    
    <%@ include file="footer.jsp" %>
    
    
</html>
