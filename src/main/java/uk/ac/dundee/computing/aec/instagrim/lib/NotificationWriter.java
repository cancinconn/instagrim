/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification.NotificationType;

/**
 *
 * @author Can
 */


public class NotificationWriter {

    //Writes notifications onto the request which will be displayed in the next page that the browser sees.
    public static void writeNotification(String notificationText, NotificationType notificationType, HttpServletRequest request)
    {
        //add this one onto pre-existing notifications if they exist, otherwise make a new lniked list and add this notification as the first element. Finally, set as attribute on request.
        LinkedList<Notification> notifications = (LinkedList<Notification>) request.getSession().getAttribute("notifications");
        if (notifications != null)
        {
            notifications.add(new Notification(notificationText, notificationType));
        } else {
            notifications = new LinkedList<Notification>();
            notifications.add(new Notification(notificationText, notificationType));
        }
        request.getSession().setAttribute("notifications", notifications);
    }
    
        //Writes notifications onto the request which will be displayed in the next page that the browser sees.
    public static void writeNotification(String notificationText, NotificationType notificationType, HttpServletRequest request, boolean keepAliveForLonger)
    {
        //expirationCount determines how many gets will need to happen (INCLUDING the current request being handled) before the notification stops getting displayed.
        int expirationCount = 1;
        if (keepAliveForLonger)
        {
            expirationCount = 2;
        }
        
        //add this one onto pre-existing notifications if they exist, otherwise make a new lniked list and add this notification as the first element. Finally, set as attribute on request.
        LinkedList<Notification> notifications = (LinkedList<Notification>) request.getSession().getAttribute("notifications");
        if (notifications != null)
        {
            notifications.add(new Notification(notificationText, notificationType, expirationCount));
        } else {
            notifications = new LinkedList<Notification>();
            notifications.add(new Notification(notificationText, notificationType, expirationCount));
        }
        request.getSession().setAttribute("notifications", notifications);
    }
    
}
