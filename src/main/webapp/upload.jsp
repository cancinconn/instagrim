<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    
    <%@ include file="header.jsp" %>
    
    <body>
        <nav>
            <ul>
                <li class="nav"><a href="${pageContext.request.contextPath}/upload.jsp">Upload</a></li>
                <li class="nav"><a href="${pageContext.request.contextPath}/Images/majed">Sample Images</a></li>
            </ul>
        </nav>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                
                <li>Picture Title: <input type="text" name="title"></li>
                
                File to upload: <input type="file" name="upfile"><br/>

                <br/>
                <input type="submit" value="Press"> to upload the file!
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="${pageContext.request.contextPath}">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
