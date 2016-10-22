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
import uk.ac.dundee.computing.aec.instagrim.models.CommentModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;

/**
 *
 * @author Can
 */
@WebServlet(name = "PostComment", urlPatterns = {"/PostComment/*"})
public class PostComment extends HttpServlet {

    Cluster cluster=null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NoHostAvailableException
    {
        if (cluster == null)
        {
            response.sendRedirect(request.getContextPath()+"/Error");
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
        
        String[] args = request.getPathInfo().split("/");
        //args[1] will be the uuid of the picture we are commenting on.
        String picUUID = args[1];
        
        //redirect to image page
        response.sendRedirect(request.getContextPath() + "/Image/" + picUUID); //use loggedIn (session attribute) to determine whose data we're altering
        
        //TODO: Handle invalid picUUIDs in Image servlet, use doesPictureExist() in the PicModel
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
        
        String[] args = request.getPathInfo().split("/");
        
        //args[1] will be the uuid of the picture we are commenting on.
        String picUUID = args[1];
        
        LoggedIn lg = (LoggedIn)request.getSession().getAttribute("LoggedIn");
        
        boolean wasLoggedIn = false;
        
        //Check if session has a logged-in user.
        if (lg != null)
        {
            if (lg.getLoggedIn())
            {
                wasLoggedIn = true;
                
                String username = lg.getUsername();
                String comment = request.getParameter("comment");
                
                PicModel picModel = new PicModel();
                picModel.setCluster(cluster);
                
                //validate comment
                boolean isCommentValid = false; //assumed false, change to true later
                
                if (comment!=null)
                {
                    if (comment.length()>0 && comment.trim().length() > 0) //comment has to be greater than length 0, not accepting all-whitespace comments either.
                    {
                        isCommentValid = true;
                    }
                }
                
                // Handle erroneous input
                if (!isCommentValid)
                {
                    NotificationWriter.writeNotification("Please make sure your comment isn't empty!", Notification.NotificationType.ERROR, request);
                    response.sendRedirect(request.getContextPath() + "/Image/" + picUUID);
                    return;
                }
                
                //validate picUUID
                boolean doesPicExist = picModel.doesPictureExist(picUUID);
                if (!doesPicExist)
                {
                    NotificationWriter.writeNotification("Please make sure you are posting your comment on a picture that exists!", Notification.NotificationType.ERROR, request);
                    response.sendRedirect(request.getContextPath() + "/Image/" + picUUID);
                    return;
                }
                
                
                //attempt to post comment
                CommentModel commentModel = new CommentModel(cluster);
                boolean wasSuccessful = commentModel.postComment(picUUID, comment, username);
                
                if (wasSuccessful)
                {
                    //redirect to image page, should show the newly-added comment
                    NotificationWriter.writeNotification("Your comment has been posted successfully.", Notification.NotificationType.INFO, request);
                    response.sendRedirect(request.getContextPath() + "/Image/" + picUUID);
                    return;
                }
                else
                {
                    NotificationWriter.writeNotification("Your comment could not be posted. Please try again.", Notification.NotificationType.ERROR, request);
                    response.sendRedirect(request.getContextPath() + "/Image/" + picUUID);
                    return;
                }
                
            }
        }
        
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
