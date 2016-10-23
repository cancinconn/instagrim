/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;
import java.nio.ByteBuffer;

/**
 *
 * @author Administrator
 */
public class Pic {
    
    private ByteBuffer bImage = null;
    private int length;
    private String type;
    private java.util.UUID UUID=null;
    private String title;
    private String username;
    
    private int width;
    private int height;
    
    public void Pic() {

    }
    public void setUsername(String name)
    {
        username = name;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    public String getSUUID(){
        return UUID.toString();
    }
    public void setPic(ByteBuffer bImage, int length, String type) {
        this.bImage = bImage;
        this.length = length;
        this.type=type;
    }

    public ByteBuffer getBuffer() {
        return bImage;
    }

    public int getLength() {
        return length;
    }
    
    public String getType(){
        return type;
    }
    
    public String getTitle()
    {
        return title;
    }
    
        public String getShortTitle()
    {
        String processedTitle = title;
        if (processedTitle != null)
        {
            if (processedTitle.contains("<br>"))
            {
                processedTitle = processedTitle.replaceAll("<br>", "");
            }
            if (processedTitle.length() > 26)
            {
                processedTitle = processedTitle.substring(0, 25) + "...";
            }
        }
        else
        {
            processedTitle = "Untitled";
        }
        return processedTitle;
    }
    
    public String getUsername()
    {
        return username;
    }

    public byte[] getBytes() {
         
        byte image[] = Bytes.getArray(bImage);
        return image;
    }
    
    public void setDimensions(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
        
    public int getAdjustedWidth(int targetSize)
    {
        if (width > height)
        {
            if (width >= targetSize)
            { //width is larger, so adjust based on width.
                return targetSize;
            } else {
                //width is less than targetsize
                return width;
            }
        }
        else
        { //height is greater than ofr equal to width
            if (height >= targetSize)
            {
                //adjust width in proportion with height
                return width*(targetSize/height);
            } else { //height is larger but still doesn't exceed targetSize, return as-is.
                return width;
            }
        }
    }
    
    public int getAdjustedHeight(int targetSize)
    {
        if (height > width)
        {
            if (height >= targetSize)
            { //width is larger, so adjust based on width.
                return targetSize;
            } else {
                //height is less than targetsize
                return height;
            }
        }
        else
        { //height is less than or equal to width
            if (width >= targetSize)
            {
                //adjust height in proportion with width
                return height*(targetSize/width);
            } else { //width is larger but still doesn't exceed targetSize, return as-is.
                return height;
            }
        }
    }

}
