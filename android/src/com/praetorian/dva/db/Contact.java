package com.praetorian.dva.db;
/**
 * 
 * @author Richard Penshorn
 *
 */
public class Contact
{
    private String name;
    private String username;
    private String email;
    public Contact(String name,String username, String email)
    {
        this.name = name;
        this.username = username;
        this.email = email;
    }
    public String getName()
    {
        return name;
    }
    public String getUsername()
    {
        return username;
    }
    public String getEmail()
    {
        return email;
    }
    public String toString()
    {
        return "Name: " + name + "\n" +
               "Email: " + email + "\n" +
               "Username" + username;
    }
    
}
