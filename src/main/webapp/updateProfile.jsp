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
                
                <div class="centerDiv2" style="margin-top:30px;">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">First Name <input type="text" name="fname"></p>
                        <p class = "pageTextInline">Last Name <input type="text" name="lname"></p>
                        <p class = "pageText"> Confirm your Password to save changes: </p>
                    </div>
                </div>
                
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Email Address <input type="text" name="email"></p>
                    </div>
                </div>
                
                <div class="centerDiv2">
                    <div class="leftInlineDiv">
                        <p class = "pageTextInline">Check this box to keep your email private: <input type="checkbox" name="isprivate" value="true"><br></p>
                    </div>
                </div>
                
                <div class="centerDiv2" style="margin-top:30px;">
                    <div class="rightInlineDiv">
                        <p class = "pageText"> Confirm your password to save changes: </p>
                        <div style ="text-align: right;">
                             <input type="password" name="password">
                        </div>
                    </div>
                </div>
                
                <br/>
                <div style="text-align: center;">
                    <input type="submit" value="Update Profile"> 
                </div>
            </form>
        
    </body>
    
    <%@ include file="footer.jsp" %>
    
</html>
