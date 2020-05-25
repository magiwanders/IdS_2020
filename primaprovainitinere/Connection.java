import java.io.*;
import java.util.*;
import java.net.*;

public class Connection implements Runnable {

    private Socket client;
    private CredentialsList users; // Lista di utenti.
    private Vector<Discussion> discussionsList; // Lista di discussioni.

    private BufferedReader inClient; // Legge dal client.
    private PrintWriter outClient; // Scrive al client.

    private String username = new String();
    private String password = new String();

    private final static boolean logIsActive = true; // True se è necessario un log delle operazioni effettuate dai client nel terminale del server.
    private final static boolean verbose = false; // True se è necessario per ogni log specificare anche la stacktrace di chiamata.

    public Connection(Vector<Discussion> discussionsList, CredentialsList users, Socket client)
    { // Tutte le connessioni condividono la stessa lista di discussioni e la stessa lista di utenti.
        this.discussionsList = discussionsList;
        this.users = users;
        this.client = client;
    }

    public void run()
    { // Struttura di esecuzione del programma (modellata a immagine del client).
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
        }
    }

    private void connect()
    { // Gestisce l'apertura dei canali di comunicazione client-server.
        try
        {
            inClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
            log("Nuovo client connesso.");
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            exit();
        }
    }

    private void login()
    { // Gestisce i tentativi di login.
        try
        {
            boolean logInSuccessful = false;
            while(!logInSuccessful)
            {
                if(inClient.readLine().equals("login"))
                {
                    username = (inClient.readLine()).replaceAll(" ", "");
                    password = (inClient.readLine()).replaceAll(" ", "");
                    log("fa un tentetivo di login.");
                    logInSuccessful = users.loginAttempt(username, password);
                    outClient.println(logInSuccessful);
                }
                else exit();
            }
            log("loggato con successo.");
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            exit();
        }
    }

    private void interact()
    { // Gestisce la scelta del client di operazione da eseguire.
        try
        {
            while(true)
            {
                String choice = inClient.readLine();
                outClient.flush();
                if(choice.equals("aggiungi")) addDiscussion();
                else if(choice.equals("discuti")) discuss();
                else if(choice.equals("migliori")) viewTenBest();
                else if(choice.equals("fine")) return;
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            exit();
        }
    }

    private void addDiscussion()
    { // Gestisce l'operazione di aggiunta di una discussione.
        try
        {
            String author = inClient.readLine();
            String title = inClient.readLine();
            discussionsList.add(new Discussion(author, title));
            log("ha inserito una nuova notizia '" + title + "'.");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            exit();
        }
    }

    private void discuss()
    { // Struttura di esecuzione per vedere e lasciare commenti.
        try
        {
            viewListOfDiscussions();
            int chosenDiscussionIndex = Integer.parseInt(inClient.readLine())-1; // -1 perchè quando vengono visualizzate all'utente le discussioni sono numerate a partire da 1, non da 0.
            if(chosenDiscussionIndex<discussionsList.size() && chosenDiscussionIndex>=0)
            {
                Discussion chosenDiscussion = discussionsList.elementAt(chosenDiscussionIndex);
                writeComment(chosenDiscussion);
            }
            else
            {
                outClient.println("0");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            exit();
        }
    }

    private void viewListOfDiscussions()
    { // Visualizza l'intera lista di discussioni.
        try
        {
            log("ha richiesto la lista di discussioni.");
            int numberOfDiscussions = discussionsList.size();
            outClient.println(numberOfDiscussions);
            for(int i=0; i<numberOfDiscussions; i++) outClient.println("[N^" + (i+1) + "] " + discussionsList.elementAt(i).toString()); // Discussioni numerate a partire da 1.
        }
        catch (NullPointerException e)
        {
            System.err.println("Non ci sono discussioni.");
            outClient.println("0");
        }
    }

    private void writeComment(Discussion chosenDiscussion)
    { // Gestisce il processo per lasciare commenti a una data discussione.
        try
        {
            sendCommentList(chosenDiscussion);
            String doYouWannaComment = inClient.readLine();
            if(doYouWannaComment.equals("si"))
            {
                int vote = Integer.parseInt(inClient.readLine());
                String comment = inClient.readLine();
                chosenDiscussion.leaveComment(username, vote, comment);
                log("ha lasciato un commento a '" + chosenDiscussion.getTitle() + "'.");
            }
         }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            exit();
        }
    }

    private void sendCommentList(Discussion chosenDiscussion)
    { // Manda al client i commenti di una data discussione.
        int numberOfComments = chosenDiscussion.getNumberOfComments();
        outClient.println(numberOfComments);
        for(int i=0; i<numberOfComments; i++)
        {
            Comment currentComment = chosenDiscussion.getComment(i);
            outClient.println(currentComment.getVote());
            outClient.println("[" + currentComment.getAuthor() + "] " + currentComment.getBody());
        }
        log("ha richiesto la lista dei commenti di '" + chosenDiscussion.getTitle() + "'.");
    }

    private void viewTenBest()
    { // Gestisce l'operazione di invio delle 10 migliori discussioni.
        try
        {
            log("ha richiesto le 10 migliori discussioni.");
            Vector<Discussion> discussionsClone = (Vector<Discussion>) discussionsList.clone(); // Unchecked casting, ma nel mio codice una discussionList può solo essere un Vector<Discussion>.
            int i;
            for(i=0; i<Math.min(10, discussionsList.size()); i++)
            {
                Discussion bestDiscussion = getBestDiscussion(discussionsClone);
                outClient.println(bestDiscussion.toString());
                discussionsClone.removeElement(bestDiscussion);
            }
            if(i<10) outClient.println("FINE");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
            exit();
        }
    }

    private Discussion getBestDiscussion(Vector<Discussion> currentList)
    { // Ritorna la discussione con la media dei voti più alta tra quelle passate alla funzione.
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
        return currentList.elementAt(indexOfHighest);
    }

    private void exit()
    { // Esegue tutte le operazioni necessarie alla chiusura della connessione.
        users.logout(username);
        log("si è disconnesso.");
        outClient.close();
        try
        {
            inClient.close();
            client.close();
        }
        catch(IOException e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    private void log(String message)
    { // Gestisce il log a terminale del server, per tracciare le varie operazioni dei client connessi.
        if(logIsActive)
        {
            if(!username.isEmpty()) message = "[" + username + "] " + message;
            if(verbose) message = transformToVerbose(message);
            System.out.println(message);
        }
        else
        {
            return;
        }
    }

    private String transformToVerbose(String message)
    { // Aggiunge la funzionalità verbose al log.
        StackTraceElement[] info = Thread.currentThread().getStackTrace(); // Recupero lo stack di attivazione.
        String nameOfCaller = info[3].toString(); // Memorizzo l'identità del blocco corrispondente all'entità che chiama log().
        String verboseHeader =  "\n"
                            +   "["
                            +    nameOfCaller
                            +   "] "
                            +   "\n";
        return verboseHeader + message;
    }

}
