import java.io.*;
import java.util.*;
import java.net.*;

public class CredentialsList
{
    // ATTRIBUTI
    private TreeMap<String,String> registeredUsers;
    private Vector<String> onlineUsers;

    // COSTRUTTORI
    public CredentialsList ()
    {
        registeredUsers = new TreeMap<String,String>();
        onlineUsers = new Vector<String>();
    }

    public CredentialsList (String username, String password)
    {
        this();
        registeredUsers.put(username, password);
    }

    public CredentialsList (String [] usernames, String [] passwords)
    {
        this();
        for(int i=0; i<usernames.length; i++)
        {
            registeredUsers.put(usernames[i], passwords[i]);
        }
    }

    // METODI
    synchronized public boolean loginAttempt(String username, String password)
    { // Verifica le credenziali passategli.
        if(!isUserAlreadyLogged(username))
        {
            if(isNewUser(username))
            {
                registeredUsers.put(username,password);
                System.out.println("[loginAttempt] Registrato nuovo username '" + username + "' con password '" + password + "'.");
                addToLogged(username);
                return true;
            }
            else if (isPasswordCorrect(username, password))
            {
                System.out.println("[loginAttempt] Password di " + username + " corretta.");
                addToLogged(username);
                return true;
            }
            else System.out.println("[loginAttempt] Password errata, richiedo immissione.");
            return false;
        }
        else
        {
            System.out.println("[loginAttempt] Login fallito.");
            return false;
        }
    }

    private boolean isNewUser(String username)
    {
        return !registeredUsers.containsKey(username);
    }

    private boolean isPasswordCorrect(String username, String password)
    {
        return password.equals(registeredUsers.get(username));
    }

    private boolean isUserAlreadyLogged(String username)
    {
        boolean isLogged = onlineUsers.contains(username);
        if(isLogged) System.out.println("[loginAttempt] Username già loggato.");
        return isLogged;
    }

    private void addToLogged(String username)
    {
        if(!onlineUsers.contains(username)) onlineUsers.add(username);
        else return;
    }

    synchronized public void logout(String username)
    {
        if(onlineUsers.contains(username)) onlineUsers.remove(username);
        else return;
    }


}
