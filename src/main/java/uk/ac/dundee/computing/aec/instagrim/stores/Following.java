/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author Can
 */
public class Following {
    
    private String username;
    private String profilePictureSUUID;
    private String time;
    
    public Following(String followedUser, String time)
    {
        this.username = followedUser;
        this.time = time;
        profilePictureSUUID = null; //initialise to null, set later if found.
    }
    
    public void setProfilePictureSUUID(String SUUID)
    {
        profilePictureSUUID = SUUID;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getTime()
    {
        return time;
    }
    
    public String getProfilePictureSUUID()
    {
        return profilePictureSUUID;
    }
    
}
