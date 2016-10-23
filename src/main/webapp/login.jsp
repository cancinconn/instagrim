<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
     <%@ include file="header.jsp" %>
    
    <body>
        <header>
            <p class = "pageTitle">Login</p>
            <p class = "pageText">Please enter your login details below.</p>
        </header>
       
        <article>
            <form method="POST"  action="Login">
                
                <div class="centerDiv2" style="margin-top:30px;">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Username <input type="text" name="username"></p>
                        <p class = "pageTextInline">Password <input type="password" name="password"></p>
                    </div>
                </div>
                <br/>
                
                <div style="text-align: center;">
                    <input type="submit" value="Log In"> 
                </div>
            </form>

        </article>
    </body>
    
    <%@ include file="footer.jsp" %>
    
</html>
