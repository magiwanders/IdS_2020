package test;

import java.io.*;
import java.util.*;
import java.net.*;
import org.junit.*;
import static org.junit.Assert.*;
import src.primaprovainitinere.*;

public class ConnectionTest {

  private ServerDummy server;
  private Thread serverT;
  private ClientDummy clientDummy;
  private Thread clientDummyT;

  private Connection connection;
  private Thread connectionT;

  private Vector<String> results;
  private int cursor = 0;

  private Vector<Discussion> discussionsList;
  private CredentialsList users;

  private static boolean firstTime = true;

  @Before
  public void setUp() {
    try {
      System.out.println("1. Inizio setUp()");
      discussionsList = new Vector<Discussion>();
      users = new CredentialsList(false);
      results = new Vector<String>();
      server = new ServerDummy();
      clientDummy = new ClientDummy(results);

      if(firstTime) {
        firstTime = false;
        serverT = new Thread(server);
        serverT.start();
        clientDummyT = new Thread(clientDummy);
        clientDummyT.start();

        connection = new Connection(discussionsList, users, server.getSocket());
        connectionT = new Thread(connection);
        System.out.println("6. Inizio thread Connection");
        connectionT.start();
      } else {
        connection = new Connection(discussionsList, users, server.getSocket());
      }
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void loginTest() {
    assertTrue(clientDummy.login("nuovoutente", "lapassword"));
    clientDummy.disconnect();
  }

  @Test
  public void emptyTest() {
    System.out.println(clientDummy + "era il calient");
    assertFalse(clientDummy.login("nuovoutente", "lapasswordsbagliata"));
    assertTrue(clientDummy.login("nuovoutente", "lapassword"));
    clientDummy.comment("0", "no");
    clientDummy.topTen();
    assertTrue(results.isEmpty());
  }
/*
  @Test
  public void populateTest() {
    clientDummy.add("Giovanni", "Notizia1");
    clientDummy.add("Giovanni", "Notizia1");
    clientDummy.add("Giovanni", "Notizia1");
  }*/

  @After
  public void closeAll() {
    try {
      server.close();
    } catch (Exception e) {
      fail();
    }
  }
}

class ServerDummy implements Runnable {

  private final static int PORT=3000;
  private ServerSocket server;
  private Socket client;

  public ServerDummy() {
    System.out.println("2. Creo ServerDummy...");
  }

  public void run() {
    System.out.println("6. Inizio thread Server");
  }

  public void initialize() {
    try {
      server = new ServerSocket(PORT);
      System.out.println("In ascolto del client");
      client = server.accept();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Socket getSocket() {
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Restituisco Socket");
    return client;
  }

  public void close() {
    try {
      server.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class ClientDummy implements Runnable {

    private final static int PORT=3000;
    private final static String address="localhost";

    private Socket socket;
    private Vector<String> results;

    private PrintWriter outSocket;
    private BufferedReader inSocket;

    public ClientDummy(Vector<String> results) {
      System.out.println("3. Creo ClientDummy");
      this.results = results;
    }

    public void run(){
      System.out.println("6. Inizio thread Client");
    }

    private void initialize() {
      try
      {
        System.out.println("4. Connetto ClientDummy a ServerDummy.");
        socket = new Socket(address, PORT);
        inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        System.out.println("5. Client connesso.");
      }
      catch(Exception e)
      {
          System.out.println("Exception: "+e);
          e.printStackTrace();
          try {
              socket.close();
          } catch(IOException ex) {
              System.err.println("Socket not closed");
          }
      }
    }

    public String report() {
      String result = new String();
      try {
        result = inSocket.readLine();
        results.add(result);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return result;
    }

    public void send(String message) {
      try {
        outSocket.println(message);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public boolean login(String user, String psw) {
      boolean loggato=false;
          send("login");
          outSocket.println(user);
          outSocket.println(psw);
          loggato=Boolean.valueOf(report()).booleanValue();
      return loggato;
    }

    public void add(String author, String title) {
      send("aggiungi");
      send(author);
      send(title);
    }

    public void comment(String chosenDiscussion, String yesOrNo, String vote, String comment) {
      send("discuti");

      int numberOfDiscussions = Integer.parseInt(report());
      for(int i=0;i<numberOfDiscussions;i++) {
          String news = report();
      }

      send(chosenDiscussion);
      int numberOfComments = Integer.parseInt(report());
      for(int i=0;i<numberOfComments;i++) {
          String voto = report();
          String commento = report();
      }

      send(yesOrNo);
      if(yesOrNo.equals("si")) {
        send(vote);
        send(comment);
      }
    }

    public void comment(String chosenDiscussion, String yesOrNo) {
      comment(chosenDiscussion, yesOrNo, "dummy1", "dummy2");
    }

    public void topTen() {
      send("migliori");
      for(int i=0;i<10;i++)
      {
          String news=report();
          if(news.equals("FINE")) break;
      }
    }

    public void disconnect() {
      send("fine");
      try {
        socket.close();
        Thread.sleep(1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
