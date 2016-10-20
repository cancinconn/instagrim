/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.NotificationWriter;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel.FilterTypes;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Notification;

/**
 *
 * @author Can
 */
@WebServlet(name = "Upload", urlPatterns = {"/Upload", "/upload"})
@MultipartConfig
public class Upload extends HttpServlet {

    private Cluster cluster;
    
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
        
        RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
        rd.forward(request, response);
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String title=null;
        String filter = null;
        FilterTypes filterType = FilterTypes.INVALID;
        InputStream is;
        InputStream imageInputStream = null;
        String type = null;
        String filename = null;
        int i = 0;
        
        //get all parts of the multipart form
        for (Part part : request.getParts()) {
            
            //keep continuing until we hit the image data
            if (part.getName().equals("title"))
            {
                is = part.getInputStream();
                title = readInputStream(is);
                continue;
            }
            if (part.getName().equals("filter"))
            {
                is = part.getInputStream();
                filter = readInputStream(is);
                System.out.println("Found filter type: " + filter);
                if (filter.equals("grim"))
                {
                    filterType = FilterTypes.GRIM;
                } else if (filter.equals("invert"))
                {
                    filterType = FilterTypes.INVERTED;
                } else if (filter.equals("gbr"))
                {
                    filterType = FilterTypes.GBR;
                } else if (filter.equals("none"))
                {
                    filterType = FilterTypes.NONE;
                } else if (filter.equals("lowdepth"))
                {
                    filterType = FilterTypes.LOWBITDEPTH;
                }
                
                continue;
            }
            if (part.getName().equals("upfile"))
            {
                System.out.println("Part Name " + part.getName());

                type = part.getContentType();
                filename = part.getSubmittedFileName();

                imageInputStream = request.getPart(part.getName()).getInputStream();
                
                continue;
            }
           
        }
        
        
         
            //error checks, input validation:
            if (title == null || title == "" || filterType == FilterTypes.INVALID)
            {
                NotificationWriter.writeNotification("Image could not be uploaded. Make sure you give the image a title.", Notification.NotificationType.ERROR, request);
                response.sendRedirect(request.getContextPath()+"/Upload");
                return;
            }
            
            //Further error checks
            if (imageInputStream != null && filename != null && type != null)
            {
                i = imageInputStream.available();
            }
            else
            {
                NotificationWriter.writeNotification("Image could not be uploaded. Make sure you choose an image to upload.", Notification.NotificationType.ERROR, request);
                response.sendRedirect(request.getContextPath()+"/Upload");
                return;
            }
            
            
            HttpSession session=request.getSession();
            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
            String username=null;
            if (lg.getLoggedIn()){
                username=lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                imageInputStream.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, username, title, filterType);

                imageInputStream.close();
            }
            
            NotificationWriter.writeNotification("Your picture has been uploaded successfully.", Notification.NotificationType.INFO, request);
            response.sendRedirect(request.getContextPath() + "/Upload");

    }
    
    private String readInputStream(InputStream is) throws IOException
    {
        String result = null;
        
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        
        try {
            String nextLine = null;
            while ((nextLine = br.readLine()) != null)
            {
                //we now know we have lines to read, so make the result an empty string which we will append to.
                if (result == null)
                {
                    result = nextLine;
                }
                else
                {
                    //append to result
                    result += nextLine + "<br>";
                }

            }
        }
        catch (IOException ex)
        {
            System.out.println("Exception occurred while reading stream: " + ex.getMessage());
        }
        finally
        {
            isr.close();
            br.close();
        }
         
        return result;
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
