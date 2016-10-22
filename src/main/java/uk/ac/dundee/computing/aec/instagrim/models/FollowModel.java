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
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import uk.ac.dundee.computing.aec.instagrim.stores.Following;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Can
 */
public class FollowModel {
    
    Cluster cluster;
    
    public FollowModel(Cluster cluster)
    {
        this.cluster = cluster;
    }
    
    public boolean createFollow(String user1, String user2)
    {
        
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("INSERT into follows (user1,user2,time) VALUES (?,?, toTimestamp( now() ) )");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(user1, user2));
        
        return true;
    }
    
    public LinkedList<Following> getFollowedUsers(String user1)
    {
        java.util.LinkedList<Following> followList = new java.util.LinkedList<>();
        
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("SELECT user2, time FROM follows WHERE user1=?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( boundStatement.bind(user1) );
        if (rs.isExhausted()) {
            System.out.println("No followed users returned.");
        } else {
            for (Row row : rs) {
                Following newFollow = new Following(row.getString("user2"), row.getTimestamp("time").toString());
                newFollow.setDate(row.getTimestamp("time"));
                followList.add(newFollow);
            }
        }
        
        //now get profile pictures
        findAndSetProfilePictures(followList);
        
        return followList;
    }
    
    public boolean removeFollow(String user1, String user2)
    {
        if (!isAlreadyFollowing(user1, user2))
        {
            return false;
        }
        else {
            Date followDate = getFollowDate(user1, user2);
            
            Session session = cluster.connect("instagrim");
            PreparedStatement ps = session.prepare("DELETE FROM follows WHERE user1 = ? AND time = ? AND user2 = ?");
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(user1, followDate, user2));

            return true;
        }
    }
    
    public boolean isAlreadyFollowing(String user1, String user2)
    {
        //iterate through list of users being followed and check for occurence.
        java.util.LinkedList<Following> followList = getFollowedUsers(user1);
        for (Following follow : followList) {

            if (follow.getUsername().equals(user2))
            {
                return true;
            }
        }
        return false;
    }
    
    private Date getFollowDate(String user1, String user2)
    {
        //iterate through list of users being followed and check for occurence.
        java.util.LinkedList<Following> followList = getFollowedUsers(user1);
        for (Following follow : followList) {

            if (follow.getUsername().equals(user2))
            {
                return follow.getDate();
            }
        }
        return new Date();
    }
    
    private LinkedList<Following> findAndSetProfilePictures(LinkedList<Following> followList)
    {
        //Use the user model to get each user's profile picture and add its SUUID to the comment object.
        User userModel = new User();
        userModel.setCluster(cluster);
        
        if (followList != null)
        {
            for (Following follow : followList) {
            
                String username = follow.getUsername();
                if (username != null)
                {
                    UUID picid = userModel.getProfilePicID(username);
                    if (picid!=null)
                    {
                        follow.setProfilePictureSUUID(picid.toString());
                    }
                }
            }
        }
        

        
        return followList;
        
    }
    
}
