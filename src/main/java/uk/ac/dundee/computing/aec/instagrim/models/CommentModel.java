/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

/**
 *
 * @author Can
 */
public class CommentModel {
    
    Cluster cluster;
    
    public CommentModel(Cluster cluster)
    {
        this.cluster = cluster;
    }
    
    public boolean postComment(String picID, String comment, String username)
    {
        UUID picUUID = UUID.fromString(picID);
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("INSERT into picturecomments (picid,comment,username,time) VALUES (?,?,?, toTimestamp( now() ) )");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( boundStatement.bind( picUUID,comment,username));
        
        return true;
    }
    
    public LinkedList<Comment> getComments(String picID)
    {
        UUID picUUID = UUID.fromString(picID);
        
        LinkedList<Comment> comments = null;
        
        Session session = cluster.connect("instagrim");
        
        PreparedStatement ps = session.prepare("SELECT comment,username,time from picturecomments where picid=? order by time ASC");
        BoundStatement boundStatement = new BoundStatement(ps);
        
        ResultSet rs = null;
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                         picUUID));

        //each row is a comment - put it into the list.
        for (Row row : rs) {
            
            if (comments == null)
            {
                comments = new LinkedList<Comment>();
            }
            
            String comment = row.getString("comment");
            String username = row.getString("username");

            String time = row.getTimestamp("time").toString();
            
            comments.add(new Comment(comment, username, time));
        }
        
        return comments;
        
    }
    
    public LinkedList<Comment> findAndSetProfilePictures(LinkedList<Comment> comments)
    {
        //Use the user model to get each user's profile picture and add its SUUID to the comment object.
        User userModel = new User();
        userModel.setCluster(cluster);
        
        if (comments != null)
        {
            for (Comment comment : comments) {
            
                String username = comment.getAuthor();
                if (username != null)
                {
                    UUID picid = userModel.getProfilePicID(username);
                    if (picid!=null)
                    {
                        comment.setProfilePictureSUUID(picid.toString());
                    }
                }
            }
        }
        

        
        return comments;
        
    }

    
}
