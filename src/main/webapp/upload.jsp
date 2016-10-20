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
 
        <article>

            <br>
            
            <form method="POST" enctype="multipart/form-data" action="Upload">
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline">Picture Title: <input type="text" name="title"></p>
                    </div>
                </div>
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline"> Filter: </p> 
                        <select name="filter">
                        <option value="grim">Instagrim</option>
                        <option value="none">Instaplain</option>
                        <option value="invert">Invertagram</option>
                        <option value="gbr">Instaswap</option>
                        <option value="lowdepth">Retrogram</option>
                        </select>
                    </div>
                </div>
                <div class="centerDiv2">
                    <div class="rightInlineDiv">
                        <p class = "pageTextInline"> File to upload:</p> <input type="file" name="upfile">
                    </div>
                </div>
                <div style="text-align: center;">
                <input type="submit" value="Press"> <p class = "pageTextInline"> to upload the file! </p>
                </div>
            </form>

        </article>
    </body>
</html>
