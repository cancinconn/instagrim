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
            
            <h1>Recently Uploaded Pictures</h1>
            
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p class="pageText">No Pictures found.</p>
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
                            <a href="${pageContext.request.contextPath}/Image/<%=p.getSUUID()%>" ><img class="picThumb" src="${pageContext.request.contextPath}/Thumb/<%=p.getSUUID()%>"></a>
                    </td>
        
        
        <%

            }

%>              </tr> <!--end the last table row-->         
            </table> <%

            }
        %>
        
        </article>
    </body>
</html>
