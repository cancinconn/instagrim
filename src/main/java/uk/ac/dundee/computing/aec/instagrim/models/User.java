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
import java.util.LinkedList;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

/**
 *
 * @author Administrator
 */
public class User {
    
    Cluster cluster;
    
    public User(){

    }
    
    public boolean registerUser(String username, String Password){
        AeSimpleSHA1 sha1handler =  new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password) Values(?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword));
        //We are assuming this always works.  Also a transaction would be good here !
        //TODO: Improve - do not assume it always works, look into transactions
        
        return true;
    }
    
    public boolean updateDetails(String userName, String password, String firstName, String lastName)
    {
        
        if (!IsValidUser(userName, password))
        {
            return false;
        }
        
        Session session = cluster.connect("instagrim");
        
        PreparedStatement ps = session.prepare("UPDATE instagrim.userprofiles" +
        " SET first_name = ?, last_name = ?" +
        " WHERE login = ?");
                
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        firstName,lastName,userName));
        
        //TODO: Don't assume this always returns true (we return false if password is wrong but do nothing for other cases here)
        return true;
                
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned"); //TODO: Figure out if this is actually about images or password being returned
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
   
    
    return false;  
    }
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
       
       
       
       public UserDetails getDetails(String username)
       {
            UserDetails details = null;    
            
           if (cluster != null)
           {
                //TODO: Get details
               
                Session session = cluster.connect("instagrim");
           
                ResultSet rs = null;
                PreparedStatement ps = null;
           
                ps = session.prepare("select first_name,last_name from userprofiles where login =?");
           
                BoundStatement boundStatement = new BoundStatement(ps);
                rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            username));
                
                if (rs.isExhausted()) {
                    System.out.println("No details returned.");
                    return details;
                } else {
                    
                    //get details
                    String firstName = null;
                    String lastName = null;
                    for (Row row : rs) { // we expect only one row here
                            firstName = (row.getString("first_name")); //get the details cassandra returns
                            lastName = (row.getString("last_name")); //get the details cassandra returns
                    }
                    //assign to store object
                    details = new UserDetails(username, firstName, lastName);
                    
                }
               
           }
           else
           {
               //error

               System.out.println("Error: Cluster was null for UserModel.");
               
           }
           
           return details;
           
       }

    
}
