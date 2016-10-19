package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
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
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.CommentModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/RawImage",
    "/RawImage/*",
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("RawImage", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);
        CommandsMap.put("Image", 4);
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        switch (command) {
            case 1: //Raw Image
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            case 2: //Images
                DisplayImageList(args[2], request, response);
                break;
            case 3: //Thumb
                DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                break;
            case 4: //request for Image Page
                ForwardToImagePage(Convertors.DISPLAY_PROCESSED, request, response, args[2]);
                break;
            default:
                error("Bad Operator", response);
        }
    }
    
    protected void ForwardToImagePage(int imageType, HttpServletRequest request, HttpServletResponse response, String uuid) throws ServletException, IOException {
        
        Pic pic = null;
        
        //get the Pic object for the uuid
        PicModel picModel = new PicModel();
        picModel.setCluster(cluster);
        pic = picModel.getPic(imageType, UUID.fromString(uuid));
        
        //get the list of comments to be displayed on the page from the CommentModel
        CommentModel commentModel = new CommentModel(cluster);
        LinkedList<Comment> comments = commentModel.getComments(uuid);
        commentModel.findAndSetProfilePictures(comments);
        
        //then send the user off to the page, with the pic passed as an attribute
        RequestDispatcher rd = request.getRequestDispatcher("/displayPicture.jsp");
        request.setAttribute("pic", pic);
        request.setAttribute("comments", comments);
        rd.forward(request, response);
        
    }

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //We also need the user Model to grab information from cassandra on whose pictures are being displayed
        User userModel = new User();
        userModel.setCluster(cluster);
        
        String[] args = request.getPathInfo().split("/");
        //args[1] will be the user's username, use it to get details
        String username = args[1];
        UserDetails userDetails = userModel.getDetails(username);
        
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Pics", lsPics);
        request.setAttribute("userDetails", userDetails);
        
        rd.forward(request, response);

    }

    private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
  
        
        Pic p = tm.getPic(type,java.util.UUID.fromString(Image));
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String title=null;
        
        for (Part part : request.getParts()) {
            
            //keep continuing until we hit the image data
            if (part.getName().equals("title"))
            {
                InputStream is = part.getInputStream();
                title = readInputStream(is);
                continue;
            }
            
            System.out.println("Part Name " + part.getName());

            String type = part.getContentType();
            String filename = part.getSubmittedFileName();
            
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session=request.getSession();
            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
            String username="majed"; // TODO: Figure out why there is a hard coded name here - also note this is the same name as the sample images
            if (lg.getLoggedIn()){
                username=lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, username, title);

                is.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
             rd.forward(request, response);
        }

    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
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
                    result = "";
                }
                //append to result
                result += nextLine + "<br>";
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
}
