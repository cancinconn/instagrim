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
       
        <article>
            <p class = "pageTitle">Create an Instagrim Account</p>
            
            <p class = "pageText">Please fill in the form below to register. Fields marked with asterisks (*) below are required.</p>
            
            <form method="POST"  action="Register">
                <div class="centerDiv2" style="margin-top:30px;">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Username* <input type="text" name="username" pattern=".{3,12}" required title="Must be at least 3 characters, only alphanumeric characters and underscores are allowed. Length must not exceed 12 characters."></p>
                    </div>
                </div>
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Password* <input type="password" name="password" pattern=".{8,}" required title="Must be at least 8 characters, only alphanumeric characters and underscores are allowed."></p>
                    </div>
                </div>
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">First Name* <input type="text" name="fname" required></p>
                    </div>
                </div>
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Last Name <input type="text" name="lname"></p>
                    </div>
                </div>
                
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Email Address* <input type="text" name="email" required></p>
                    </div>
                </div>
                
                <div class="centerDiv2">
                    <div class="leftInlineDiv">
                        <p class = "pageTextInline">Check this box to keep your email private: <input type="checkbox" name="isprivate" value="true"><br></p>
                    </div>
                </div>
         
                <br/>
                
                <div style="text-align: center;">
                    <input type="submit" value="Register"> 
                </div>
                
            </form>

        </article>
    </body>
    
    <%@ include file="footer.jsp" %>
    
    
</html>
