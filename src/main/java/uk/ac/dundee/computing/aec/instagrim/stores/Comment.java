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
    
    public Comment(String commentText, String authorname, String time)
    {
        this.commentText = commentText;
        author = authorname;
        this.time = time;
    }
    
    public String getCommentText()
    {
        return commentText;
    }
    
    public String getAuthor()
    {
        return author;
    }
    
    public String getTime()
    {
        return time;
    }
    
}
