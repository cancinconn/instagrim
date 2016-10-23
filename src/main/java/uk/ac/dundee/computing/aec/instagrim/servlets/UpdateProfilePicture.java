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
import java.util.UUID;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.NotificationWriter;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;

/**
 *
 * @author Can
 */
@WebServlet(name = "UpdateProfilePicture", urlPatterns = {"/UpdateProfilePicture/*"})
public class UpdateProfilePicture extends HttpServlet {
    
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, boolean isGET)
            throws ServletException, IOException {
        
        String[] args = request.getPathInfo().split("/");
        
        //args[1] will be the desired profile picture's uuid.
        String picUUID = args[1];
        
        LoggedIn lg = (LoggedIn)request.getSession().getAttribute("LoggedIn");
        
        boolean wasSuccessful = false;
        if (lg!=null)
        {
            if (lg.getLoggedIn() && lg.getUsername() != null)
            {
                User user=new User();
                user.setCluster(cluster);
                
                PicModel picModel = new PicModel();
                picModel.setCluster(cluster);
                //check if picture belongs to user
                if (!picModel.doesPictureBelongToUser(UUID.fromString(picUUID), lg.getUsername()))
                {
                    NotificationWriter.writeNotification("You can only use one of your own pictures as your profile picture!", Notification.NotificationType.ERROR, request, isGET);
                    response.sendRedirect(request.getContextPath()+"/Image/"+picUUID);
                    return;
                }
                wasSuccessful = user.updatePicture(lg.getUsername(), picUUID);
            }
        }
        
        if (wasSuccessful)
        {
            response.sendRedirect(request.getContextPath()+"/Profile/"+lg.getUsername()); //lg.getUsername cannot be null if we were successful. This is safe.
            return;
        }
        else
        {
            NotificationWriter.writeNotification("We could not set this image as your profile picture.", Notification.NotificationType.ERROR, request, isGET);
            response.sendRedirect(request.getContextPath()+"/Image/"+picUUID);
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
