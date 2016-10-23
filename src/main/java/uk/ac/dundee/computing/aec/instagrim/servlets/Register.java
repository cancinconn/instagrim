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
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register", "/register"})
public class Register extends HttpServlet {
    Cluster cluster=null;
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
        String username=request.getParameter("username");
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
        
        
        //Do Input validation:
        if (username == null || username.equals("") || password == null || password.equals(""))
        {
            //Write error message:
            NotificationWriter.writeNotification("Please ensure that your username or password field is not empty!", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Register");
            return; //return so that we do not process this request.
        }
        
        //Input validation: enforce lowercase usernames
        username = username.toLowerCase();
        
        //Input validation: valid characters
        if (!username.matches("^[a-zA-Z0-9_]*$") || !password.matches("^[a-zA-Z0-9_]*$")) //only a through z, 0 through 9, and underscores allowed.
        {
            //Write error message:
            NotificationWriter.writeNotification("Please ensure that your username and password are made up only of alphanumeric characters and underscores!", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Register");
            return; //return so that we do not process this request.
        }
        
        //Do Input validation - length:
        if (username.length() < 3 || password.length() < 8 || username.length()>12)
        {
            //Write error message:
            NotificationWriter.writeNotification("Please ensure that your username and password fields have proper lengths!", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Register");
            return; //return so that we do not process this request.
        }
        
        //Do Input validation - first name:
        if (firstName == null || firstName.equals(""))
        {
            //Write error message:
            NotificationWriter.writeNotification("Please provide your first name.", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Register");
            return; //return so that we do not process this request.
        }
        
        //Do Input validation:
        if (email == null || email.equals("") || !email.contains("@") || !email.contains(".") || email.length() < 5)
        {
            //Write error message:
            NotificationWriter.writeNotification("Please ensure that your email address is valid!", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Register");
            return; //return so that we do not process this request.
        }
        
        User user=new User();
        user.setCluster(cluster);
        
        //check if username already exists:
        if (!user.isUsernameAvailable(username))
        {
            //Write error message:
            NotificationWriter.writeNotification("That username is already in use! Please pick another one.", Notification.NotificationType.ERROR, request);

            response.sendRedirect(request.getContextPath()+"/Register");
            return; //return so that we do not do any more processing on this request.
        }
        
        user.registerUser(username, password);
        user.updateDetails(username, password, firstName, lastName, email, isEmailPrivate);
        
	Login.loginSession(username, password, request, response, cluster, true);
        
    }
    
    
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
        
        boolean isUserLoggedIn = false;
        LoggedIn lg = (LoggedIn) request.getAttribute("LoggedIn");
        if (lg != null)
        {
            if(lg.getLoggedIn())
            {
                isUserLoggedIn = true;
            }
        }
        
        if (isUserLoggedIn)
        {
            //Show error saying the user is logged in - they must log out before registering a new account:
            NotificationWriter.writeNotification("You are logged in! You must log out before registering with a new account.", Notification.NotificationType.ERROR, request, true);
            response.sendRedirect(request.getContextPath());
        }
        else
        {
            RequestDispatcher rd=request.getRequestDispatcher("register.jsp");
	    rd.forward(request,response);
        }
        
        
    }
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet handles the registration process.";
    }// </editor-fold>

}
