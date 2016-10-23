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
public class UserDetails {
    
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    
    private boolean isEmailPrivate;
    
    public UserDetails()
    {
        
    }
    
    public UserDetails(String uName, String fName, String lName, String email, boolean isPrivate)
    {
        setDetails(uName, fName, lName, email, isPrivate);
    }
    
    
    //One mutator method for everything:
    
    public final void setDetails(String uName, String fName, String lName, String email, boolean isPrivate)
    {
        username = uName;
        firstName = fName;
        lastName = lName;
        this.email = email;
        isEmailPrivate = isPrivate;
    }
    
    //Accessor methods:
    public String getUsername()
    {
        return username;
    }
    
    public String getFirstName()
    {
        if (firstName.equals("")) lastName = " ";
        return firstName;
    }
    
    public String getLastName()
    {
        if (lastName.equals("")) lastName = " ";
        return lastName;
    }
    
    public boolean getIsEmailPrivate()
    {
        return isEmailPrivate;
    }
    public String getEmail()
    {
        if (email.equals("")) email = "Unknown";
        return email;
    }
    
}
