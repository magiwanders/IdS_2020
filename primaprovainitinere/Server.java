/******************************************************************************
 * Prima Prova in Itinere
 * Nome: Simone Shawn Cazzaniga
 * Matricola: 952966
 *****************************************************************************/

import java.io.*;
import java.util.*;
import java.net.*;

public class Server
{ // Gestisce le connessioni in ingresso e le smista ognuna in un thread. (può inizializzare users e discussioni)
    private final static int PORT=3000;

    private ServerSocket server;
    private Socket client;
    private CredentialsList users; // Oggetto che contiene e gestisce users e passwords.
    private Vector<Discussion> discussionsList; // Lista di tutte le discussioni disponibili.

    private final static boolean initialize = true; // True se si vuole un certo numero di discussioni e utenti già inseriti.


    // COSTRUTTORE

    public Server()
    {
        System.out.println("Server avviato.");

        discussionsList = new Vector<Discussion>();
        users = new CredentialsList();

        if(initialize) initialize(); // Inizializza una decina di utenti e una decina di discussioni con qualche commento.

        try
        {
            server = new ServerSocket(PORT);
            listenForConnections();
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        finally
        {
            try {
                server.close();
            } catch(IOException e) {
                System.err.println("ServerSocket not closed.");
            }

            try {
                client.close();
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }


    //METODI

    private void listenForConnections()
    { // Smista le connessioni in entrata.
        try
        {
            while(true)
            {
                client = server.accept();
                new Thread(new Connection(discussionsList, users, client)).start();
            }
        }
        catch(IOException e)
        {
            System.err.println("One of the connections has failed.");
        }

    }


    // METODI DI INIZIALIZZAZIONE (per aggiungere utenti e discussioni predefinite.)

    private String [] presetUserList()
    {
        String [] presetUserList = {"user0",
                                    "user1",
                                    "user2",
                                    "user3",
                                    "user4",
                                    "user5",
                                    "user6",
                                    "user7",
                                    "user8",
                                    "user9",
                                    "magi",
                                    "gospodin",
                                    "a1fa,num3r1c0'"};
        return presetUserList;
    }

    private String [] presetPasswordList()
    {
        String [] presetPasswordList = {"psw0",
                                        "psw1",
                                        "psw2",
                                        "psw3",
                                        "psw4",
                                        "psw5",
                                        "psw6",
                                        "psw7",
                                        "psw8",
                                        "psw9",
                                        "magi245676531",
                                        "pulsoreplicazione",
                                        "pswA14a,numer1c@''"};
        return presetPasswordList;
    }

    private void initialize()
    {
        users = new CredentialsList(presetUserList(), presetPasswordList());

        Discussion d1 = new Discussion("Rossi", "Notizia generica prima.");
        d1.leaveComment("user0", 2, "Non apprezzo.");
        d1.leaveComment("user8", 10, "Ottima notizia!");
        d1.leaveComment("magi", 8, "Anche io d'accordo.");
        d1.leaveComment("user5", 5, "Mi dispiace è insipida.");
        d1.leaveComment("user2", 4, "C'è di molto meglio");
        Discussion d2 = new Discussion("Brambilla", "Altra notizia generica.");
        d2.leaveComment("user2", 5, "Scritta male.");
        d2.leaveComment("user1", 5, "Non è scientificamente provata!");
        d2.leaveComment("user3", 8, "Godibile, nonostante tutto.");
        Discussion d3 = new Discussion("Panzeri", "Controversie generiche.");
        d3.leaveComment("user7", 7, "Ha ragione.");
        d3.leaveComment("user6", 0, "Tutte fesserie!");
        d3.leaveComment("a1fa,num3r1c0'", 1, "Non si può sentire!");
        d3.leaveComment("user9", 8, "Niente da dire.");
        d3.leaveComment("user8", 8, "Anche io d'accordo!");
        Discussion d4 = new Discussion("Colombo", "Notizia sull'ambiente");
        d4.leaveComment("user0", 4, "Prima l'economia!");
        d4.leaveComment("user6", 9, "Una giusta causa. Bisogna educare la gente.");
        d4.leaveComment("user2", 3, "Il cambiamento climatico è una bufala!.");
        d4.leaveComment("user7", 7, "Molto importante creare consapevolezza.");
        d4.leaveComment("user6", 7, "Subito in piazza.");
        Discussion d5 = new Discussion("Esposito", "Nessuno ha letto questa notizia.");
        Discussion d6 = new Discussion("Bassani", "Recensione generica di un libro.");
        d6.leaveComment("user4", 4, "Banale, letteratura da tavolino.");
        d6.leaveComment("user6", 10, "Capolavoro contemporaneo.");
        d6.leaveComment("user7", 9, "Mi è molto piaciuto!");
        Discussion d7 = new Discussion("Del Ferro", "Notizia sulla pandemia.");
        d7.leaveComment("user3", 9, "Applauso ai lavoratori sanitari!");
        d7.leaveComment("magi", 5, "Qui finiamo tutti senza lavoro.");
        d7.leaveComment("user6", 4, "La fine è vicina. Lo dico da anni!");
        d7.leaveComment("user7", 8, "Ne usciremo tutti insieme!");
        d7.leaveComment("user9", 9, "Buona fortuna Italia.");
        Discussion d8 = new Discussion("Maggioni", "Articolo di cucina.");
        d8.leaveComment("user4", 7, "Non mi si è montata bene la panna, ma a parte quello molto buono.");
        d8.leaveComment("user2", 4, "Non spiega niente, mica siamo tutti cuochi nati!");
        d8.leaveComment("user0", 10, "Consigli sensazionali per una buona ricetta.");
        d8.leaveComment("user8", 2, "Non ho capito niente. Quindi la pasta non deve attarsi al muro per essere pronta?");
        d8.leaveComment("user7", 7, "Buone idee per un pranzo con gli amici.");
        Discussion d9 = new Discussion("Bianchi", "Notizia poco interessante.");
        d9.leaveComment("gospodin", 2, "Chi ha dato la licenza di giornalista al signor Bianchi?");
        Discussion d10 = new Discussion("Redaelli", "Annuncio editoriale.");
        d10.leaveComment("user3", 9, "Finalmente, dopo tanti anni esce una nuova ristampa.");
        d10.leaveComment("user2", 10, "Opera finora introvabile, che fortuna!");
        Discussion d11 = new Discussion("Ferrari", "Notizia sul mercato automobilistico.");
        d11.leaveComment("user9", 6, "Poco significativo, tanto non potrò mai permettermelo.");
        d11.leaveComment("user7", 2, "Ciarlate da radical chic!");
        d11.leaveComment("a1fa,num3r1c0'", 0, "Nessuno ha chiesto questo articolo.");
        d11.leaveComment("user5", 3, "Perchè non ci date notizie vere?");
        Discussion d12 = new Discussion("Fabbri", "Articolo pubblicitario.");
        d12.leaveComment("user2", 6, "Prodotto nella media.");
        d12.leaveComment("magi", 8, "Ottimo prodotto! Mi hanno fatto lo sconto.");
        d12.leaveComment("user0", 7, "Pubblicità mediamente onesta.");
        d12.leaveComment("user4", 8, "Non delude mai!");
        Discussion d13 = new Discussion("Panelli", "Articolo scientifico generico.");
        d13.leaveComment("user6", 10, "L'universo è magnifico.");
        d13.leaveComment("user5", 10, "Molto godibile anche per i non adetti ai lavori!");
        d13.leaveComment("user9", 2, "Secondo me la terra non è così tonda.");
        d13.leaveComment("user1", 9, "Buon articolo, buona scienza.");
        d13.leaveComment("user4", 8, "Molto piaciuto.");
        Discussion d14 = new Discussion("Mariani", "Articolo musicale.");
        d14.leaveComment("gospodin", 1, "Questo qui non ci capisce niente di musica.");
        d14.leaveComment("user1", 5, "Un po' commerciale ma godibile.");
        Discussion d15 = new Discussion("Romano", "Articolo Culturale.");
        d15.leaveComment("a1fa,num3r1c0'", 7, "Non l'avrei mai detto.");
        d15.leaveComment("user2", 3, "Ma cosa mi serve saperlo?");
        d15.leaveComment("user1", 6, "Già noto per chiunque abbia fatto il classico.");
        d15.leaveComment("user8", 4, "Non utile come pensavo.");

        discussionsList.add(d1);
        discussionsList.add(d2);
        discussionsList.add(d3);
        discussionsList.add(d4);
        discussionsList.add(d5);
        discussionsList.add(d6);
        discussionsList.add(d7);
        discussionsList.add(d8);
        discussionsList.add(d9);
        discussionsList.add(d10);
        discussionsList.add(d11);
        discussionsList.add(d12);
        discussionsList.add(d13);
        discussionsList.add(d14);
        discussionsList.add(d15);
    }


    // MAIN

    public static void main (String [] args)
    {
        Server s = new Server();
    }

}
