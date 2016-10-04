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
    
    public UserDetails()
    {
        
    }
    
    public UserDetails(String uName, String fName, String lName)
    {
        setDetails(uName, fName, lName);
    }
    
    
    //One mutator method for everything:
    
    public final void setDetails(String uName, String fName, String lName)
    {
        username = uName;
        firstName = fName;
        lastName = lName;
    }
    
    //Accessor methods:
    public String getUsername()
    {
        return username;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
        public String getLastName()
    {
        return lastName;
    }
    
}
