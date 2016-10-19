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
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    Cluster cluster=null;
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
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
        
        //Error check
        if (username == null || password == null)
        {
            //TODO: Handle NULL username and/or password
        }
        
        User user=new User();
        user.setCluster(cluster);
        user.registerUser(username, password);
        user.updateDetails(username, password, firstName, lastName);
        
	response.sendRedirect(request.getContextPath());
        
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
            //TODO: Show error page saying the user is logged in - they must log out before registering a new account:
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
