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
            
        <!-- Presentation logic for when the user is NOT found -->
        <%
        UserDetails userDetails = (UserDetails) request.getAttribute("userDetails");
        if (userDetails == null) {
        %>
        <p class="pageText">User not found.</p>
        <% } else {
        //Presentation logic for when the user IS found -->
        %>
            
            <h1>Pictures by <a href="${pageContext.request.contextPath}/Profile/<%=userDetails.getUsername()%>"><%=userDetails.getUsername()%></a></h1>
            
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p class="pageText">No Pictures found</p>
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
        
        
        <%
} //END PRESENTATION LOGIC
%>   
        </article>
    </body>
</html>
