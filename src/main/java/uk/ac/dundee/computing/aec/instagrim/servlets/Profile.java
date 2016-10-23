/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import java.util.LinkedList;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.NotificationWriter;
import uk.ac.dundee.computing.aec.instagrim.models.FollowModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.Following;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

/**
 *
 * @author Can
 */
@WebServlet(name = "Profile", urlPatterns = {"/Profile", "/Profile/*", "/profile", "/profile/*"})
public class Profile extends HttpServlet {
    
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
        
        //TODO: Read username from URL
        
        //Initialise user and pic models which will grab data from cassandra
        User userModel = new User();
        userModel.setCluster(cluster);
        
        PicModel picModel = new PicModel();
        picModel.setCluster(cluster);
        
        FollowModel followModel = new FollowModel(cluster);
        
        if (request.getPathInfo() == null)
        {
            NotificationWriter.writeNotification("Please specify which user's profile you would like to view!", Notification.NotificationType.ERROR, request, isGET);
            response.sendRedirect(request.getContextPath());
            return;
        }
        
        String[] args = request.getPathInfo().split("/");
        
        if (args.length <= 1)
        {
            NotificationWriter.writeNotification("Please specify which user's profile you would like to view!", Notification.NotificationType.ERROR, request, isGET);
            response.sendRedirect(request.getContextPath());
            return;
        }
        
        //args[1] will be the user's username, use it to get details
        String username = args[1];
        username = username.toLowerCase();
        
        //Get user details
        UserDetails userDetails = userModel.getDetails(username);
        request.setAttribute("userDetails", userDetails);
        
        //Get Profile pic
        UUID picid = userModel.getProfilePicID(username);
        Pic profilePic =  picModel.getPic(Convertors.DISPLAY_THUMB, picid);
        request.setAttribute("profilePic", profilePic);
        
        //Get follows
        LinkedList<Following> followList = new LinkedList<>();
        if (userDetails != null)
        {
             followList = followModel.getFollowedUsers(username);
        }
        request.setAttribute("follows", followList);
        
        //are we following this person?
        boolean isFollowing = false; //assume false, change later
        
        LoggedIn lg = (LoggedIn)request.getSession().getAttribute("LoggedIn");
        boolean isLoggedIn = false;
        //Check if session has a logged-in user.
        if (lg != null)
        {
            if (lg.getLoggedIn() && lg.getUsername() != null)
            {
                isLoggedIn = true;
            }
        }
        
        if (isLoggedIn)
        {
            isFollowing = followModel.isAlreadyFollowing(lg.getUsername(), username);
        }
        request.setAttribute("isFollowing", isFollowing);
        
        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
        rd.forward(request, response);
        
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
