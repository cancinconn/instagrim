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
        
        <!-- Display Comment Box if logged in -->
        <% //determine if the user is logged in
        boolean isLoggedIn = false;
        if (lg != null)
        {
            if (lg.getLoggedIn())
            {
                isLoggedIn = true;
            }else{
                isLoggedIn = false;
            }
        } else {
            isLoggedIn = false;
        }
        %>
            
            <% if (isLoggedIn) { %>
                
            <form method="POST"  action="${pageContext.request.contextPath}/PostComment/<%=pic.getSUUID()%>">
                <div class="buttonDiv">
                    <textarea rows="4" cols="50" name="comment"> </textarea>
                </div>
                <div class="buttonDiv">
                    <input class="button" type="submit" value="Comment"> 
                </div>
            </form>
                
            <%} else {%>
            
            <p class = "pageText">Sign in to post comments on pictures.</p> <br>
            
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
            <p class = "pageText"><%=comments.get(i).getAuthor()%>: <%=comments.get(i).getCommentText()%> (<%=comments.get(i).getTime().toString() %>)</p>
            <%}%>
            

        <%}%>
        
        <%} //End of page display logic %>
        

        
    </body>
</html>

