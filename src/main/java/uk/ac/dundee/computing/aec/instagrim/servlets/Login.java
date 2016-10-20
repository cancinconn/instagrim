/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.NotificationWriter;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Login", urlPatterns = {"/Login", "/login"})
public class Login extends HttpServlet {

    Cluster cluster=null;


    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    
    public static void loginSession(String username, String password, HttpServletRequest request, HttpServletResponse response, Cluster cluster, boolean isFirstLogin) throws IOException
    {
        User us=new User();
        us.setCluster(cluster);
        boolean isValid=us.isValidUser(username, password);
        HttpSession session=request.getSession();
        System.out.println("Session in servlet "+session);
       
        
        if (isValid){
            LoggedIn lg= new LoggedIn();
            lg.setLoggedIn();
            lg.setUsername(username);
            //request.setAttribute("LoggedIn", lg);
            
            session.setAttribute("LoggedIn", lg);
            System.out.println("Session in servlet "+session);
            
            //Let the user know they've logged in:
            if (isFirstLogin)
            {
                NotificationWriter.writeNotification("Your account has been created. You are now logged in.", Notification.NotificationType.INFO, request);
            }else{
                NotificationWriter.writeNotification("You have been logged in successfully.", Notification.NotificationType.INFO, request);
            }
            
            
            //Send redirect, because we need to redirect to avoid form resubmission on refresh.
            response.sendRedirect(request.getContextPath());
            
        }else{
            //Let user know of error:
            NotificationWriter.writeNotification("Login failed. Please ensure you enter your login details correctly.", Notification.NotificationType.ERROR, request);
            //Send redirect, because we need to redirect to avoid form resubmission on refresh.
            response.sendRedirect("/Instagrim/Login");
        }
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
        
        loginSession(username, password, request, response, cluster, false);
        
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
            //TODO: Show error page saying the user is already logged in:
            response.sendRedirect(request.getContextPath());
        }
        else
        {
            RequestDispatcher rd=request.getRequestDispatcher("login.jsp");
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
        return "Short description";
    }// </editor-fold>

}
