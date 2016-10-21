package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import javax.imageio.ImageIO;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Following;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {
    
    public enum FilterTypes
    {
        GRIM,
        INVERTED,
        THUMB,
        NONE,
        GBR,
        LOWBITDEPTH,
        INVALID
    }

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    public java.util.LinkedList<Pic> getFeedPics(String username) {
        java.util.LinkedList<Pic> results = new java.util.LinkedList<>();
        
        LinkedList<LinkedList<Pic>> picLists = new LinkedList<LinkedList<Pic>>();
        
        //get the list of followed people, using the FollowModel
        FollowModel followModel = new FollowModel(cluster);
        LinkedList<Following> followList = followModel.getFollowedUsers(username);
        
        for (Following follow : followList)
        {
            picLists.add(getRecentPicsByUser(follow.getUsername(), 10));
        }
        
        int resultsCount = 0;
        int targetCount = 12;
        int emptyCounter = 0;
        boolean searchComplete = false; //assume false, set to true when it's over
        
        while (resultsCount < targetCount && searchComplete == false)
        {
           for (LinkedList<Pic> picList : picLists)
           {
               //stop if we have enough pics or if all our lists are empty.
               if (resultsCount >= targetCount || emptyCounter >= picLists.size())
               {
                   searchComplete = true;
                   break;
               } 
               
               if (picList != null)
               {
                    if (!picList.isEmpty())
                    {
                        emptyCounter = 0; //reset empty counter
                        results.add(picList.get(0));
                        resultsCount++;
                        picList.remove(picList.get(0));
                        continue;
                    }
                    else
                    {
                        //one of the lists is empty, add to empty counter so we know when to stop:
                        emptyCounter++;
                    }
               } else {
                   //a null list of pics counts as an empty list.
                   emptyCounter++;
               }

           }
        }

        //Finally, return the list of pictures we retrieved!
        return results;
    }
            
        public java.util.LinkedList<Pic> getRecentPicsByUser (String user, int limit) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT picid FROM userpiclist WHERE user = ? LIMIT ?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( boundStatement.bind(user, limit) );
        if (rs.isExhausted()) {
            //No pictures returned.
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                pic.setUUID(UUID);
                Pics.add(pic);
            }
        }
        return Pics;
    }    
            
    public java.util.LinkedList<Pic> getRecentPics () {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT picid FROM userpiclist LIMIT 21");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( boundStatement.bind() );
        if (rs.isExhausted()) {
            //No pictures returned.
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                pic.setUUID(UUID);
                Pics.add(pic);
            }
        }
        return Pics;
    }

    public void insertPic(byte[] b, String type, String name, String user, String title, FilterTypes filterType) {
        try {
            Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
            //TODO: Look into this:
            //The following is a quick and dirty way of doing this, will fill the disk quickly!
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            
            System.out.println("APPLYING FILTER: " + filterType.toString());
            
            //process and get byte array:
            byte[] processedb = getPictureBytes(picid.toString(), types[1], processPicture(picid.toString(),types[1], filterType));
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            
            //process with filter, then process again to get thumbnail, and finally get byte array:
            byte [] thumbb = getPictureBytes(picid.toString(), types[1], processPicture(FilterTypes.THUMB, processPicture(picid.toString(),types[1], filterType) ) );
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            

            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name,title) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name, title));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }
    
    //This method pre-processes the picture before it is turned into a byte-array.
    public BufferedImage processPicture(String picid, String type, FilterTypes filterType) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processedImage;

            switch (filterType) {
                case GRIM:
                    processedImage = createGrayscale(BI);
                    break;
                case INVERTED:
                    processedImage = createInverted(BI);
                    break;
                case THUMB:
                    processedImage = createThumbnail(BI);
                    break;
                case GBR:
                    processedImage = createGBR(BI);
                    break;
                case NONE:
                    processedImage = createPlain(BI);
                    break;
                case LOWBITDEPTH:
                    processedImage = createLowDepth(BI);
                    break;
                default:
                    processedImage = createGrayscale(BI);
                    break;
            }

            return processedImage;
        } catch (IOException et) {

        }
        return null;
    }

    //This method pre-processes the picture before it is turned into a byte-array. (this 2nd overload allows chaining)
    public BufferedImage processPicture(FilterTypes filterType, BufferedImage srcImage) {
        try {
            BufferedImage BI = srcImage;
            BufferedImage processedImage;

            switch (filterType) {
                case GRIM:
                    processedImage = createGrayscale(BI);
                    break;
                case INVERTED:
                    processedImage = createInverted(BI);
                    break;
                case THUMB:
                    processedImage = createThumbnail(BI);
                    break;
                case GBR:
                    processedImage = createGBR(BI);
                    break;
                case NONE:
                    processedImage = createPlain(BI);
                    break;
                case LOWBITDEPTH:
                    processedImage = createLowDepth(BI);
                    break;
                default:
                    processedImage = createGrayscale(BI);
                    break;
            }

            return processedImage;
        } catch (Exception et) {

        }
        return null;
    }
    
    public byte[] getPictureBytes (String picid,String type, BufferedImage srcImage) {
        try {
            BufferedImage processedImage = srcImage;
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processedImage, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createGrayscale(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        //img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS);
        // Let's add a little border before we return result.
        //return pad(img, 2);
        // Change of plan - Let's not.
        return img;
    }
    
   public static BufferedImage createGrayscale(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        //return pad(img, 4);
        return img;
    }
   
    public static BufferedImage createInverted(BufferedImage img) {
        
        for (int x = 0; x<img.getWidth()-1; x++)
        {
            for (int y = 0; y<img.getHeight()-1; y++)
            {
                //get colors as separate components, invert, then set.
                int red = new Color(img.getRGB(x, y)).getRed();
                int green = new Color(img.getRGB(x, y)).getGreen();
                int blue = new Color(img.getRGB(x, y)).getBlue();
                
                int outputRGB = new Color(255-red, 255-green, 255-blue).getRGB();
                
                img.setRGB(x, y, outputRGB);
            }
        }
        
        return img;
    }
    
    public static BufferedImage createGBR(BufferedImage img) {
        
        for (int x = 0; x<img.getWidth()-1; x++)
        {
            for (int y = 0; y<img.getHeight()-1; y++)
            {
                //get colors as separate components, invert, then set.
                int red = new Color(img.getRGB(x, y)).getRed();
                int green = new Color(img.getRGB(x, y)).getGreen();
                int blue = new Color(img.getRGB(x, y)).getBlue();
                
                int outputRGB = new Color(green, blue, red).getRGB();
                
                img.setRGB(x, y, outputRGB);
            }
        }
        
        return img;
    }
    
    public static BufferedImage createPlain(BufferedImage img) {
       
        return img;
    }
    
    public static BufferedImage createLowDepth(BufferedImage img) {
        
        for (int x = 0; x<img.getWidth()-1; x++)
        {
            for (int y = 0; y<img.getHeight()-1; y++)
            {
                //get colors as separate components, invert, then set.
                int red = new Color(img.getRGB(x, y)).getRed();
                int green = new Color(img.getRGB(x, y)).getGreen();
                int blue = new Color(img.getRGB(x, y)).getBlue();
                
                int stepSize = 64;
                
                //divide up the colour space into fewer parts, change colour to these.
                for (int i=0; i<255; i+=stepSize)
                {
                    if (i<=red && red < i+stepSize)
                    {
                        red=i+stepSize;
                        break;
                    }
                }
                for (int i=0; i<255; i+=stepSize)
                {
                    if (i<=green && green < i+stepSize)
                    {
                        green=i+stepSize;
                        break;
                    }
                }
                for (int i=0; i<255; i+=stepSize)
                {
                    if (i<=blue && blue < i+stepSize)
                    {
                        blue=i+stepSize;
                        break;
                    }
                }
                
                if (red > 255) red = 255;
                if (green > 255) green = 255;
                if (blue > 255) blue = 255;
                
                int outputRGB = new Color(red, green, blue).getRGB();
                
                img.setRGB(x, y, outputRGB);
            }
        }
        
        return img;
    }
    
    
   
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            //No pictures returned
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                pic.setUUID(UUID);
                Pics.add(pic);

            }
        }
        return Pics;
    }
    
    public boolean doesPictureExist(UUID picUUID)
    {
        Session session = cluster.connect("instagrim");
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        ps = session.prepare("SELECT picid from pics where picid = ? LIMIT 1"); //only need to find 1 matching picid to see if it exists, no use wasting time trying to find another
        BoundStatement boundStatement = new BoundStatement(ps);
        
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picUUID));

            if (rs.isExhausted()) {
                return false;
            } else {
                return true;
            }
    }
    
    //Alternative overload where picID is in string format.
    public boolean doesPictureExist(String picUUID)
    {
        return doesPictureExist(UUID.fromString(picUUID));

    }

    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String username = null;
        String type = null;
        String title = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select user,title,processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        username = row.getString("user");
                        title = row.getString("title");
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);
        p.setUUID(picid); //safe to set picid now that we've found it in the database.
        
        if (username != null)
        {
            p.setUsername(username);
        }
        if (title != null)
        {
            p.setTitle(title);
        }
        else
        {
            p.setTitle("Picture");
        }
        
        return p;

    }

}
