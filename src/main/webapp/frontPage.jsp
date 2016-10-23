<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    
    <%@ include file="header.jsp" %>
    
    <body>
 
        <article>
            
            <p class = "pageTitle">Recently uploaded pictures by our users</p>
            
            <% if (!isLoggedIn) {%>
                <p class = "pageText"><a href="${pageContext.request.contextPath}/Register">Sign up</a> to Instagrim now to start sharing your pictures with friends!</p>
            <% } %>
            
            
            <br>
            
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null || lsPics.size() == 0) {
        %>
        <p class="pageText">No pictures found.</p>
        <%
        } else {

%>                 <table align="center"> <%

            int counter = -1; //keeps track of the position of the picture we are displaying
            int rowCount = 3; //how many pictures do we display in one row?
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) 
            {
                Pic p = (Pic) iterator.next();
                counter++;
        %>
        
        <!-- Put a new row in every time counter is divisible by rowCount. -->
                        <% if ((counter % rowCount) == 0)
                        { %>
                        </tr> <!--end the last table row-->
                        <tr> <!-- start a new table row-->
                        <%
                        } %>

                     <!-- display the image -->
                    <td>
                        <div class="feedCellDiv">
                            <div class="feedInnerDivUpper">
                                <a href="${pageContext.request.contextPath}/Image/<%=p.getSUUID()%>" ><img class="picFeed" src="${pageContext.request.contextPath}/Thumb/<%=p.getSUUID()%>"></a>
                            </div>
                            <div class="feedInnerDivLower" >
                                <p class="pageText" style="margin:0px;"><a href="${pageContext.request.contextPath}/Image/<%=p.getSUUID()%>" ><%=p.getShortTitle()%></a></p>
                            </div>
                        </div>
                    </td>
        
        
        <%

            }

%>              </tr> <!--end the last table row-->         
            </table> <%

            }
        %>
        
        </article>
    </body>
    
    <%@ include file="footer.jsp" %>
    
</html>
