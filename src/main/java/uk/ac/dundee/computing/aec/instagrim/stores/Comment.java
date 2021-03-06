/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import java.util.Date;

/**
 *
 * @author Can
 */
public class Comment {
    
    private String commentText;
    private String author;
    private String time;
    private String profilePictureSUUID;
    
    public Comment(String commentText, String authorname, String time)
    {
        this.commentText = commentText;
        author = authorname;
        this.time = time;
        profilePictureSUUID = null; //initialise to null, set later if found.
    }
    
    public void setProfilePictureSUUID(String SUUID)
    {
        profilePictureSUUID = SUUID;
    }
    
    public String getCommentText()
    {
        //process the string to have html-style line breaks.
        String processedText = commentText;
        processedText = processedText.replaceAll("\n", "<br>");
        return processedText;
    }
    
    public String getAuthor()
    {
        return author;
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
