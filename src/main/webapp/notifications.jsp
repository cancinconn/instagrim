<%-- 
    Document   : error
    Created on : 19-Oct-2016, 21:07:34
    Author     : Can
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.Notification.NotificationType"%>
<%@page import="java.util.LinkedList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>



<% LinkedList<Notification> notification = (LinkedList<Notification>)request.getSession().getAttribute("notifications");
    if(notification != null) 
{%>

    <%for (Notification note : notification) //can't be named notification as that's already defined in the java code underneath
    {%>


    <% if (note.getType() == NotificationType.INFO) 
    { %>

        <div class="notificationInfoDiv">

            <p class="notificationText"> <%=note.getText()%> <p>
            
        </div>

    <%} else if (note.getType() == NotificationType.ERROR) 
    { %>

        <div class="notificationErrorDiv">

            <p class="notificationText"> <%=note.getText()%> <p>
            
        </div>

    <%}%>
    
    


    <%}%>

<%}%>