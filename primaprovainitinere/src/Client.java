package primaprovainitinere.src;

import java.net.*;
import java.io.*;


public class Client
{
    private final static int PORT=3000;
    private final static int NUMSPAZI=100;
    private final static int TempoFisso=500;
    private final static int TempoVariabile=50;
    
    private final static String address="localhost";
    
    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;
    
    public Client()
    {
        System.out.println("Client avviato");
        
        try
        {
            esegui();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            // Always close it:
            try {
                socket.close();
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }
    
    private void connetti()
    {
        try
        {
            System.out.println("Il client tenta di connettersi");

            socket = new Socket(address, PORT);
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);
            
            System.out.println("Client connesso");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }
    
    
    private void esegui()
    {
        try
        {
            connetti();
            login();
            interagisci();
            chiudi();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            // Always close it:
            try {
                socket.close();
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }
    
    private void interagisci()
    {
        while(true)
        {
            outVideo.println("Scegliere cosa si vuol fare");
            outVideo.println("a --> aggiungi notizia");
            outVideo.println("c --> commenti su una notizia");
            outVideo.println("v --> vedi 10 migliori");
            outVideo.println("q --> esci");
            
            try
            {
                String scelta=inKeyboard.readLine();

                if(scelta.equals("a"))
                    aggiuntanews();
                else if(scelta.equals("c"))
                    vediCommenti();
                else if(scelta.equals("v"))
                    vediMigliori();
                else if(scelta.equals("q"))
                {
                    outSocket.println("fine");
                    outVideo.println("ARRIVEDERCI!!!!!");
                    break;
                }
                else
                    outVideo.println("INPUT ERRATO");
            }
            catch(Exception e)
            {
                System.out.println("Exception: "+e);
                e.printStackTrace();
                
                // Always close it:
                try
                {
                    socket.close();
                }
                catch(IOException ex)
                {
                    System.err.println("Socket not closed");
                }
            }
        }
    }
    
    private void aggiuntanews()
    {
        try
        {
            //codice partite
            outVideo.println("Inizio inserimento dati di una notizia\n");
            outSocket.println("aggiungi");
            
            outVideo.println("Inserisci cognome giornalista\n");
            String autore=inKeyboard.readLine();
            outSocket.println(autore);
            
            outVideo.println("Inserisci titolo\n");
            String titolo=inKeyboard.readLine();
            outSocket.println(titolo);
            
            outVideo.println("Fine inserimento dati\n");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
                   
            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }
    
    private void vediCommenti()
    {
        vediListaLibri();
        leggiCommentinews();
        aggiungiCommento();
    }
    
    private void vediListaLibri()
    {
        try
        {
            //codice partite
            outVideo.println("Commenti");
            
            outSocket.println("discuti");
            
            outVideo.println("Lista notizie in discussione:");
            int numLibri=Integer.parseInt(inSocket.readLine());
            
            for(int i=0;i<numLibri;i++)
            {
                String news=inSocket.readLine();
                outVideo.println(news);
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
                   
            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }
    
    private void vediMigliori()
    {
        try
        {
            //codice partite
            outVideo.println("Migliori");
            
            outSocket.println("migliori");
            
            outVideo.println("Lista:");
            
            for(int i=0;i<10;i++)
            {
                String news=inSocket.readLine();
                
                if(news.equals("FINE"))
                    break;
                
                outVideo.println(news);
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
                   
            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void leggiCommentinews()
    {
        try
        {
            //codice partite
            outVideo.println("Scegli");
            String id=inKeyboard.readLine();
            
            outSocket.println(id);
            int numCommenti=Integer.parseInt(inSocket.readLine());
            
            for(int i=0;i<numCommenti;i++)
            {
                String voto=inSocket.readLine();
                outVideo.println(voto);
                String commento=inSocket.readLine();
                outVideo.println(commento);
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
                   
            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void aggiungiCommento()
    {
        try
        {
            //codice partite
            outVideo.println("Vuoi aggiungere commento? (S/N)");
            String risposta=inKeyboard.readLine();
            
            if(risposta.equalsIgnoreCase("S"))
            {
                outSocket.println("si");
                
                outVideo.println("Inserisci Voto");
                String voto=inKeyboard.readLine();
                outSocket.println(voto);
            
                outVideo.println("Inserisci Commento");
                String commento=inKeyboard.readLine();
                outSocket.println(commento);
            }
            else
                outSocket.println("no");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
                   
            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }
    }

    private void generaBianco()
    {
        for(int i=0;i<NUMSPAZI;i++)
            outVideo.println();
    }
    
    private void chiudi()
    {
        try
        {
            socket.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
        finally
        {
            // Always close it:
            try
            {
                socket.close();
            }
            catch(IOException ex)
            {
                System.err.println("Socket not closed");
            }
        }
    }


    private void login()
    {
        try
        {
            boolean loggato=false;
            
            while(!loggato)
            {
                outVideo.println("Inserire login:");
                String username=inKeyboard.readLine();
                
                outVideo.println("Inserire password:");
                String password=inKeyboard.readLine();
                
                outSocket.println("login");
                outSocket.flush();
                outSocket.println(username);
                outSocket.flush();
                outSocket.println(password);
                outSocket.flush();
                
                loggato=Boolean.valueOf(inSocket.readLine()).booleanValue();
                
                if(loggato)
                    outVideo.println("Login effettuato correttamente");
                else
                    outVideo.println("Nome utente in uso con altra password");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        
            try {
                socket.close();
            }
            catch(IOException ex)
            {
                System.err.println("Socket not closed");
            }
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Client c=new Client();
    }
}