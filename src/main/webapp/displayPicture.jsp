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
            <p class = "pageTitle"><%=pic.getTitle()%></p>
        </div>
        <!-- Display pic (output comes from Image servlet, written as an image in the response) -->
        <img class="pic"  src="${pageContext.request.contextPath}/RawImage/<%=pic.getSUUID()%>"><br/>
        
        <!-- Display author -->
        <p class = "pageText">by <a href="${pageContext.request.contextPath}/Profile/<%=pic.getUsername()%>"><%=pic.getUsername()%></a></p>
         
        <!-- resize notification-->
        <%if ( pic.getWidth() > 600 || pic.getHeight() > 600)
        {%>
            <div class ="notificationInfoDiv2">
                <p class = "pageText">This image has been resized. <a href="${pageContext.request.contextPath}/RawImage/<%=pic.getSUUID()%>">View Original Size</a></p>
            </div>
        <%}%>
        
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
        
        <!-- Display Comment Box if logged in -->
            
            <% if (isLoggedIn) { %>
                
            <form method="POST"  action="${pageContext.request.contextPath}/PostComment/<%=pic.getSUUID()%>">
                <div class="buttonDiv">
                    <textarea rows="4" cols="50" name="comment"> </textarea>
                </div>
                <div class="buttonDiv">
                    <input type="submit" value="Post Comment"> 
                </div>
            </form>
                
            <%} else {%>
            
            <p class = "pageText"><a href="${pageContext.request.contextPath}/Login">Log in</a> to post comments on pictures!</p> <br> <br>
            
            <% } %>
        
        <!-- DISPLAY COMMENTS -->
        
        <% 
        LinkedList<Comment> comments = (LinkedList<Comment>)request.getAttribute("comments");
        if (comments == null)
        {
        //PRESENTATION LOGIC FOR WHEN THERE ARE NO COMMENTS:
        %>
        
            <p class = "pageText">There are no comments for this picture... :/ Why not write one?</p>
        
        <%}else{
        //PRESENTATION LOGIC FOR COMMENTS:
        %>

            <% for (int i=0; i<comments.size(); i++)
            {%>
            
            <!-- div to contain comment -->
            <div class="commentContainerDiv">
                <div class="commentLeftDiv">
                    <!-- profile picture -->
                    <div class="commentPicDiv">
                        
                        <% if(comments.get(i).getProfilePictureSUUID() == null) 
                        {%>
                            <a class = "blankCommentPic" href="${pageContext.request.contextPath}/Profile/<%=comments.get(i).getAuthor()%>" ></a>
                        <%} else {%>
                            <a href="${pageContext.request.contextPath}/Profile/<%=comments.get(i).getAuthor()%>" ><img class = "commentPic" src="${pageContext.request.contextPath}/Thumb/<%=comments.get(i).getProfilePictureSUUID()%>"></a>
                        <%}%>
                    </div>
                    <!-- comment author -->
                    <p class = "pageText"><a href="${pageContext.request.contextPath}/Profile/<%=comments.get(i).getAuthor()%>"><%=comments.get(i).getAuthor()%></a></p>
                </div>
                <!-- comment -->
                <div class="commentTextDiv">
                    <p class = "pageText"><%=comments.get(i).getCommentText()%></p>
                </div>
                <div class="commentSpaceDiv"></div>
                <!-- time -->
                <div class="commentBottomDiv">
                    <p class = "commentTimeText">(<%=comments.get(i).getTime().toString() %>)</p>
                </div>
            </div>
            <br>
            <%}%>
            

        <%}%>
        
        <%} //End of page display logic %>
        

        
    </body>
    
    <%@ include file="footer.jsp" %>
    
</html>

