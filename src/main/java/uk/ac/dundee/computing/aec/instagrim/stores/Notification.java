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
public class Notification {
    
    public enum NotificationType
    {
        INFO,
        ERROR
    }
    
    private final String text;
    private final NotificationType type;
    private int expirationCounter = 1;
    
    public Notification(String text, NotificationType type)
    {
        this.text = text;
        this.type = type;
    }
    
    public Notification(String text, NotificationType type, int expirationCount)
    {
        this.text = text;
        this.type = type;
        this.expirationCounter = expirationCount;
    }
    
    public String getText()
    {
        return text;
    }
    
    public NotificationType getType()
    {
        return type;
    }
    
    public void decrementExpirationCounter()
    {
        expirationCounter--;
    }
    
    public int getExpirationCounter()
    {
        return expirationCounter;
    }
    
}
