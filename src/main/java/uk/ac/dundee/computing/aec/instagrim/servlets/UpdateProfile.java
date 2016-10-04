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
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.*;

/**
 *
 * @author Can
 */
@WebServlet(name = "UpdateProfile", urlPatterns = {"/UpdateProfile", "/UpdateProfile/*"})
public class UpdateProfile extends HttpServlet {
    
        private Cluster cluster;
    
        
    @Override
    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
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
        
        response.sendRedirect(request.getContextPath() + "/" + "updateProfile.jsp"); //use loggedIn (session attribute) to determine whose data we're altering
        
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
        
        //WHERE YOU LEFT OFF - TODO: make updateProfile.jsp, make this servlet's post method update the user details and make get redirect to updateProfile.jsp, then test if the details change in the actual user profile view.
        
        
        //REMOVE WHEN DONE: String username=request.getParameter("username"); //figure out a way to do this without passing the username explicitly
        
        String username = null;
        
        //getting LoggedIn object from session to extract username
        LoggedIn lg = (LoggedIn) request.getSession().getAttribute("LoggedIn");
        
        if (lg != null) {
                if (lg.getLoggedIn()) {
                    username = lg.getUsername();
                }
            }
        
        String password=request.getParameter("password");
        String firstName=request.getParameter("fname");
        String lastName=request.getParameter("lname");
        
        //Error check
        if (username == null || password == null)
        {
            //TODO: Handle NULL username and/or password
        }
        
        User user=new User();
        user.setCluster(cluster);
        user.updateDetails(username, password, firstName, lastName);
        
        if (username!= null)
        {
            	response.sendRedirect(request.getContextPath()+"/Profile/"+username);
        }
        else
        {
            response.sendRedirect(request.getContextPath());
        }
        
        //TODO: Make an error page that displays an error and redirect all errors there - use a stored error object for it
        

        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles requests for changes to a user's profile.";
    }// </editor-fold>

}
