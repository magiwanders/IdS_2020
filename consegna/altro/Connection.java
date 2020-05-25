package provainitinere;

import java.io.*;
import java.util.*;
import java.net.*;

public class Connection implements Runnable {

    private Socket client;
    private int id;
    private CredentialsList users;
    private Vector<Discussion> discussionsList;

    private BufferedReader inClient;
    private PrintWriter outClient;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;

    private String username = new String();
    private String password = new String();

    private final static boolean logIsActive = true;
    private final static boolean verbose = true;

    public Connection(Vector<Discussion> discussionsList, CredentialsList users, Socket client, int id)
    {
        this.discussionsList = discussionsList;
        this.users = users;
        this.client = client;
        this.id = id;
    }

    public void run()
    {
        try {
            connect();
            login();
            interact();
            exit();
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void connect()
    {
        try
        {
            inClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
            log("connesso.");
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void login()
    {
        try
        {
            boolean logInSuccessful = false;
            while(!logInSuccessful)
            {
                if(inClient.readLine().equals("login"))
                {
                    username = (inClient.readLine()).replaceAll(" ", "");
                    password = (inClient.readLine()).replaceAll(" ", "");
                    logInSuccessful = loginAttempt();
                    outClient.println(logInSuccessful);
                }
                else exit();
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void interact()
    {
        try
        {
            while(true)
            {
                String choice = inClient.readLine();
                outClient.flush();
                if(choice.equals("aggiungi")) addDiscussion();
                else if(choice.equals("discuti")) sendCommentsToClient();
                else if(choice.equals("migliori")) viewTenBest();
                else if(choice.equals("fine")) return;
                else log("ha inviato un input non riconosciuto.");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void addDiscussion()
    {
        try
        {
            String author = inClient.readLine();
            String title = inClient.readLine();
            if(!author.equals("") && !title.equals(""))
            {
                discussionsList.add(new Discussion(author, title));
                log("ha inserito una nuova notizia.");
            }
            else
            {
                if(author.equals("")) log("non ha inserito l'autore.");
                if(title.equals("")) log("non ha inserito il titolo");
                log("Notizia non inserita.");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            log("si è disconnesso.");
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void sendCommentsToClient()
    {
        try
        {
                viewListOfDiscussions();
                Discussion currentDiscussion;
                int indexOfDiscussion = Integer.parseInt(inClient.readLine())-1;
                if(indexOfDiscussion<discussionsList.size())
                {
                    currentDiscussion = discussionsList.elementAt(indexOfDiscussion);
                    int numberOfComments = currentDiscussion.getNumberOfComments();
                    outClient.println(numberOfComments);
                    for(int i=0; i<numberOfComments; i++)
                    {
                        Comment currentComment = currentDiscussion.getComment(i);
                        outClient.println(currentComment.getVote());
                        outClient.println("[" + currentComment.getAuthor() + "] " + currentComment.getBody());
                    }

                    writeComment(indexOfDiscussion);
                }
                else throw new NumberFormatException();

        }
        catch (NumberFormatException ne)
        {
            try{
                log("ha selezionato una discussione inesistente.");
                outClient.println(0); // Number Of Comments
                if (inClient.readLine().equals("si")); // Want to comment
                {
                    inClient.readLine(); // Vote
                    inClient.readLine(); // Comment
                }
                log("Non ha inserito alcun commento.");

            } catch(IOException e2) {
                System.err.println("Client boken.");
            }
            return;
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void viewListOfDiscussions()
    {
        try
        {
            log("ha richiesto la lista di discussioni.");
            int numberOfDiscussions = discussionsList.size();
            outClient.println(numberOfDiscussions);
            for(int i=0; i<numberOfDiscussions; i++)
            {
                outClient.println("[N^" + (i+1) + "] " + discussionsList.elementAt(i).toString());
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void writeComment(int indexOfDiscussion)
    {
        try
        {
            String doYouWannaComment = inClient.readLine();
            if(doYouWannaComment.equals("si"))
            {
                int vote = Integer.parseInt(inClient.readLine());
                String comment = inClient.readLine();
                discussionsList.elementAt(indexOfDiscussion).leaveComment(username, vote, comment);
                log("ha lasciato un commento a " + discussionsList.elementAt(indexOfDiscussion).getTitle());
            }
            else if(doYouWannaComment.equals("no")) return;
            return;
         }
         catch (NumberFormatException ne)
         {
             log("ha inserito un input non riconosciuto");
             outClient.println(0);
             try {
                 inClient.readLine();
             } catch(IOException e2) {
                 System.err.println("Client boken.");
             }
             return;
         }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void viewTenBest()
    {
        try
        {
            log("ha richiesto le 10 migliori discussioni.");
            Vector<Discussion> discussionsClone = (Vector<Discussion>) discussionsList.clone();
            int i;
            for(i=0; i<min(10, discussionsList.size()); i++)
            {
                int bestDiscussion = getIndexOfHighestAverage(discussionsClone);
                outClient.println(discussionsClone.elementAt(bestDiscussion).toString());
                discussionsClone.removeElementAt(bestDiscussion);
            }
            if(i<10) outClient.println("FINE");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            try {
                client.close();
            } catch(IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

/*    private boolean loginAttempt()
    {
        if(username.equals("") || password.equals(""))
        {
            return false;
            log("Password");
            if(users.contains(username))
            {
                users.addNewUser(username, password);
                log("Ricevuto nuovo username '" + username + "' -> Registrato con password '" + password + "' -> Loggato con successo.");
                return true;
            }
            else if (users.isPasswordCorrect(username, password))
            {
                log("Password corretta. Loggato con successo.");
                return true;
            }
            else log("Password errata, richiedo immissione.");
            return false;
        }
    }
*/
    private void exit()
    {
        log("si è disconnesso.");

        try
        {
            client.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                client.close();
            }
            catch(IOException ex)
            {
                System.err.println("Socket not closed");
            }
        }
    }

    private int getIndexOfHighestAverage(Vector<Discussion> currentList)
    {
        float highestSoFar = 0;
        int indexOfHighest = 0;
        for(int i=0; i<currentList.size();i++)
        {
            float candidateToHighest = currentList.elementAt(i).getAverageOfVotes();
            if(candidateToHighest > highestSoFar)
            {
                highestSoFar = candidateToHighest;
                indexOfHighest = i;
            }
        }
        return indexOfHighest;
    }
/*
    private void log(String message)
    {
        if(logIsActive)
        {
            if(username.isEmpty()) message = "[" + id + "] " + message;
            else message = "[" + username + "] " + message;
            if(verbose) message = transformToVerbose(message);
            outVideo.println(message);
        }
        else
        {
            return;
        }
    }

    private String transformToVerbose(String message)
    {
        StackTraceElement[] info = Thread.currentThread().getStackTrace(); // Recupero lo stack di attivazione.
        String nameOfCaller = info[3].toString(); // Memorizzo l'identità del blocco corrispondente all'entità che chiama log().
        String verboseHeader =  "\n"
                            +   "["
                            +    nameOfCaller
                            +   "] "
                            +   "\n";
        return verboseHeader + message;

    }

    private int min(int a ,int b)
    {
        if(a<b) return a;
        else return b;
    }
*/

}
