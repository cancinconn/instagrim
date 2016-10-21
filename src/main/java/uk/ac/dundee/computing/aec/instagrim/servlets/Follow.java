/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.NotificationWriter;
import uk.ac.dundee.computing.aec.instagrim.models.FollowModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

/**
 *
 * @author Can
 */
@WebServlet(name = "Follow", urlPatterns = {"/Follow/*", "/follow/*"})
public class Follow extends HttpServlet {

    
    private Cluster cluster;
        
    
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }
        
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, boolean isGET)
            throws ServletException, IOException {
        
        //check if the user is logged in
        LoggedIn lg = (LoggedIn) request.getSession().getAttribute("LoggedIn");
        boolean isUserLoggedIn=false;
        if (lg!= null)
        {
            if(lg.getLoggedIn() && lg.getUsername()!=null)
            {
                isUserLoggedIn = true;
            }
        }
        
        //error handling
        if (!isUserLoggedIn)
        {
            //we make a distinction between GET and POST because method needs to be kept alive for longer in the case of GET. 
            if (isGET)
            {
                NotificationWriter.writeNotification("You are not logged in. Please log in before following others!", Notification.NotificationType.ERROR, request, true);
            }else{
                NotificationWriter.writeNotification("You are not logged in. Please log in before following others!", Notification.NotificationType.ERROR, request);
            }
            
            response.sendRedirect(request.getContextPath()+"/Login");
            return;
        }
            

        
        String[] args = request.getPathInfo().split("/");
        //args[1] will be the user's username, use it to get details
        String username = args[1];
        
         //error handling
        if (username == null || username.equals(""))
        {
            //we make a distinction between GET and POST because method needs to be kept alive for longer in the case of GET. 
            if (isGET)
            {
                NotificationWriter.writeNotification("Cannot follow user: username not valid.", Notification.NotificationType.ERROR, request, true);
            }else{
                NotificationWriter.writeNotification("Cannot follow user: username not valid.", Notification.NotificationType.ERROR, request);
            }
            
            response.sendRedirect(request.getContextPath());
            return;
        }
        
        //error handling for narcissists
        if (username.equals(lg.getUsername()))
        {
            //we make a distinction between GET and POST because method needs to be kept alive for longer in the case of GET. 
            if (isGET)
            {
                NotificationWriter.writeNotification("You cannot follow yourself!", Notification.NotificationType.ERROR, request, true);
            }else{
                NotificationWriter.writeNotification("You cannot follow yourself!", Notification.NotificationType.ERROR, request);
            }
            
            response.sendRedirect(request.getContextPath());
            return;
        }
        
        
        //We need the User Model to grab information from cassandra on whose pictures are being displayed
        User userModel = new User();
        userModel.setCluster(cluster);
        boolean doesUserExist = !userModel.isUsernameAvailable(username);
        
        if (doesUserExist)
        {
            //Use the FollowModel to talk to cassandra.
            FollowModel followModel = new FollowModel(cluster);
            
            //final bit of error handling: make sure we're not following the same person twice!
            if (followModel.isAlreadyFollowing(lg.getUsername(), username))
            {
                //we make a distinction between GET and POST because method needs to be kept alive for longer in the case of GET. 
                if (isGET)
                {
                    NotificationWriter.writeNotification("You cannot follow a user you are already following!", Notification.NotificationType.ERROR, request, true);
                }else{
                    NotificationWriter.writeNotification("You cannot follow a user you are already following!", Notification.NotificationType.ERROR, request);
                }

                response.sendRedirect(request.getContextPath() + "/Profile/" + username);
                return;
            }
            
            
            
            //this user exists, so we can follow them.
            followModel.createFollow(lg.getUsername(), username);
            
            //we make a distinction between GET and POST because method needs to be kept alive for longer in the case of GET. 
            if (isGET)
            {
                NotificationWriter.writeNotification("You are now following " + username + "!", Notification.NotificationType.INFO, request, true);
            }else{
                NotificationWriter.writeNotification("You are now following " + username + "!", Notification.NotificationType.INFO, request);
            }
            
            response.sendRedirect(request.getContextPath() + "/Profile/" + username);
            return;
            
        }
        else
        {
            //error handling
               //we make a distinction between GET and POST because method needs to be kept alive for longer in the case of GET. 
               if (isGET)
               {
                   NotificationWriter.writeNotification("Cannot follow user: username not found.", Notification.NotificationType.ERROR, request, true);
               }else{
                   NotificationWriter.writeNotification("Cannot follow user: username not found.", Notification.NotificationType.ERROR, request);
               }

               response.sendRedirect(request.getContextPath());
               return;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, true);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response, false);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
