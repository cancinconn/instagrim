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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.NotificationWriter;
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
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NoHostAvailableException
    {
        if (cluster == null)
        {
            response.sendRedirect(request.getContextPath()+"/Error");
        }
        super.service(request,response);
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
        
        RequestDispatcher rd = request.getRequestDispatcher("/updateProfile.jsp");
        rd.forward(request,response);
        
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
        
        
        String username = null;
        
        //getting LoggedIn object from session to extract username
        LoggedIn lg = (LoggedIn) request.getSession().getAttribute("LoggedIn");
        
        boolean isLoggedIn = false; //assume false, change later
        
        if (lg != null) {
                if (lg.getLoggedIn()) {
                    username = lg.getUsername();
                    isLoggedIn = true;
                }
            }
        
        String password=request.getParameter("password");
        String firstName=request.getParameter("fname");
        String lastName=request.getParameter("lname");
        String email=request.getParameter("email");
        boolean isEmailPrivate = false;
        
        String[] isPrivateValues = request.getParameterValues("isprivate");
        
        if (isPrivateValues != null && isPrivateValues.length > 0)
        {
            if (isPrivateValues[0].equals("true")) isEmailPrivate = true;
        }
        else
        {
            isEmailPrivate = false;
        }
        
        //basic error check
        if (!isLoggedIn)
        {
             NotificationWriter.writeNotification("You must log in before you can update your profile details!", Notification.NotificationType.ERROR, request);
             response.sendRedirect(request.getContextPath());

            return;
        }
        
        //Error check - in case there's an error with the username - shouldn't happen unless future code introduces a bizarre bug. Best to be safe.
        if (username == null || username.equals(""))
        {
            NotificationWriter.writeNotification("Something went wrong. We could not update your profile information - please try again later.", Notification.NotificationType.ERROR, request);
            if (username!= null)
            {
                response.sendRedirect(request.getContextPath()+"/Profile/"+username);
            }
            else
            {
                response.sendRedirect(request.getContextPath());
            }
            return;
        }
        
        //Error check: password
        if (password == null || password.equals(""))
        {
            NotificationWriter.writeNotification("Cannot update your profile details. Ensure that you enter your password to verify the changes!", Notification.NotificationType.ERROR, request);
            response.sendRedirect(request.getContextPath()+"/Profile/"+username);
            return;
        }
        
        //Do Input validation - first name:
        if (firstName == null || firstName.equals(""))
        {
            //Write error message:
            NotificationWriter.writeNotification("Cannot update your profile details. Please provide your first name.", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Profile/"+username);
            return; //return so that we do not process this request.
        }
        
        //Do Input validation:
        if (email == null || email.equals("") || !email.contains("@") || !email.contains(".") || email.length() < 5)
        {
            //Write error message:
            NotificationWriter.writeNotification("Cannot update your profile details. Please ensure that your email address is valid!", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Profile/"+username);
            return; //return so that we do not process this request.
        }
        

        
        User user=new User();
        user.setCluster(cluster);
        boolean wasSuccessful = user.updateDetails(username, password, firstName, lastName, email, isEmailPrivate);
        
        if (wasSuccessful)
        {
            NotificationWriter.writeNotification("Your profile information has been changed successfully.", Notification.NotificationType.INFO, request);
        } else {
            NotificationWriter.writeNotification("Your profile information could not be updated. Ensure that you enter your password correctly.", Notification.NotificationType.ERROR, request);
        }
        
        
        response.sendRedirect(request.getContextPath()+"/Profile/"+username);

        
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
