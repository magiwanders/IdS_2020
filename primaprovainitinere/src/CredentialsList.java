package primaprovainitinere.src;

import java.io.*;
import java.util.*;
import java.net.*;

public class CredentialsList
{
    // ATTRIBUTI
    private TreeMap<String,String> registeredUsers;
    private Vector<String> onlineUsers;

    // COSTRUTTORE
    public CredentialsList (boolean initialize)
    {
        registeredUsers = new TreeMap<String,String>();
        onlineUsers = new Vector<String>();

        if(initialize) initialize();
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


    // INIZIALIZZAZIONE

    private void initialize() {
      registeredUsers.put("user0","psw0");
      registeredUsers.put("user1","psw1");
      registeredUsers.put("user2","psw2");
      registeredUsers.put("user3","psw3");
      registeredUsers.put("user4","psw4");
      registeredUsers.put("user5","psw5");
      registeredUsers.put("user6","psw6");
      registeredUsers.put("user7","psw7");
      registeredUsers.put("user8","psw8");
      registeredUsers.put("user9","psw9");
      registeredUsers.put("magi","magi245676531");
      registeredUsers.put("gospodin","pulsoreplicazione");
      registeredUsers.put("a1fa,num3r1c0'","pswA14a,numer1c@''");
    }

}
