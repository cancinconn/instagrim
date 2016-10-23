/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
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
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;

/**
 *
 * @author Can
 */
@WebServlet(name = "Unfollow", urlPatterns = {"/Unfollow/*", "/unfollow/*"})
public class Unfollow extends HttpServlet {

    
    private Cluster cluster;
        
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NoHostAvailableException
    {
        if (cluster == null)
        {
            response.sendRedirect(request.getContextPath()+"/Error");
        }
        super.service(request,response);
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param isGET are we processing a GET request?
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
            NotificationWriter.writeNotification("You are not logged in. Please log in before unfollowing others!", Notification.NotificationType.ERROR, request, isGET);
            
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
            NotificationWriter.writeNotification("Cannot unfollow user: username not valid.", Notification.NotificationType.ERROR, request, isGET);
            
            response.sendRedirect(request.getContextPath());
            return;
        }
        
        //Use the FollowModel to talk to cassandra.
        FollowModel followModel = new FollowModel(cluster);
        
        boolean wasSuccessful = followModel.removeFollow(lg.getUsername(),username);
        
        if (wasSuccessful)
        {

            NotificationWriter.writeNotification("You are no longer following " + username + ".", Notification.NotificationType.INFO, request, isGET);
            response.sendRedirect(request.getContextPath() + "/Profile/"+lg.getUsername());
            return;
        } else {
            NotificationWriter.writeNotification("Cannot unfollow " + username + " since you are not followng any users by this name.", Notification.NotificationType.ERROR, request, isGET);
            response.sendRedirect(request.getContextPath() + "/Profile/"+lg.getUsername());
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
