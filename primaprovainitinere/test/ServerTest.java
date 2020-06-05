// THIS TEST REQUIRES THE SERVER TO BE ALREADY RUNNING

package primaprovainitinere.test;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;
import org.junit.*;
import static org.junit.Assert.*;
import primaprovainitinere.src.*;

public class ServerTest {

  private final static int PORT=3000;
  private final static String address="localhost";

  private final static int NUMBER_OF_TEST_SOCKETS=10;

  private Socket [] socket = new Socket[NUMBER_OF_TEST_SOCKETS];
  private BufferedReader [] inSocket = new BufferedReader[NUMBER_OF_TEST_SOCKETS];
  private PrintWriter [] outSocket = new PrintWriter[NUMBER_OF_TEST_SOCKETS];

  boolean [] connected;
  private int selected;

  @Before
  public void setUp() {
    socket = new Socket[NUMBER_OF_TEST_SOCKETS];
    inSocket = new BufferedReader[NUMBER_OF_TEST_SOCKETS];
    outSocket = new PrintWriter[NUMBER_OF_TEST_SOCKETS];
    connected = new boolean[NUMBER_OF_TEST_SOCKETS];
    for(int i=0; i<NUMBER_OF_TEST_SOCKETS; i++) {
      connected[i] = false;
    }
  }

  private boolean connect() {
    try {
        socket[selected] = new Socket(address, PORT);
        inSocket[selected] = new BufferedReader(new InputStreamReader(socket[selected].getInputStream()));
        outSocket[selected] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket[selected].getOutputStream())), true);
        connected[selected] = true;
        return true;
    } catch(Exception e) {
        System.out.println("Exception: " + e);
        e.printStackTrace();
        return false;
    }
  }

  private boolean disconnect() {
    try {
        send("fine");
        socket[selected].close();
        connected[selected] = false;
        return true;
    } catch (Exception e) {
        System.out.println("Exception: "+e);
        e.printStackTrace();
        return false;
    }
  }

  @Test
  public void masterTest() {
    // LOGIN

      // Connessione semplice - prima registrazione
      select(0);
      assertTrue(connect());
      send("login", "testUser", "testPassword");
      assertTrue(Boolean.valueOf(read()).booleanValue());
      // User gia loggato
      select(1);
      assertTrue(connect());
      send("login", "testUser", "testPassword");
      assertFalse(Boolean.valueOf(read()).booleanValue());
      // Sblocco user
      select(0);
      assertTrue(disconnect());
      select(1);
      send("login", "testUser", "testPassword");
      assertTrue(Boolean.valueOf(read()).booleanValue());
      assertTrue(disconnect());
      // Password errata
      select(0);
      assertTrue(connect());
      send("login", "testUser", "wrongTestPassword");
      assertFalse(Boolean.valueOf(read()).booleanValue());
      // Password Corretta di user già registrato
      send("login", "testUser", "testPassword");
      assertTrue(Boolean.valueOf(read()).booleanValue());
      // Seconda registrazione
      select(1);
      assertTrue(connect());
      send("login", "testUser1", "testPassword1");
      assertTrue(Boolean.valueOf(read()).booleanValue());
      select(0);
      assertTrue(disconnect());
      select(1);
      assertTrue(disconnect());
      // Interferenze
        // Connessione insieme
        select(0);
        assertTrue(connect());
        select(1);
        assertTrue(connect());
        send("login", "testUser1", "testPassword1");
        assertTrue(Boolean.valueOf(read()).booleanValue());
        select(0);
        send("login", "testUser", "testPassword");
        assertTrue(Boolean.valueOf(read()).booleanValue());
        assertTrue(disconnect());
        select(1);
        assertTrue(disconnect());
        // Dopo errore password errata
        select(0);
        assertTrue(connect());
        send("login", "testUser", "wrongTestPassword");
        assertFalse(Boolean.valueOf(read()).booleanValue());
        select(1);
        assertTrue(connect());
        send("login", "testUser1", "testPassword1");
        assertTrue(Boolean.valueOf(read()).booleanValue());
        select(0);
        send("login", "testUser", "testPassword");
        assertTrue(Boolean.valueOf(read()).booleanValue());
        assertTrue(disconnect());
        // Dopo errore user già loggato
        assertTrue(connect());
        send("login", "testUser1", "testPassword1");
        assertFalse(Boolean.valueOf(read()).booleanValue());
        select(2);
        assertTrue(connect());
        send("login", "testUser2", "testPassword2");
        assertTrue(Boolean.valueOf(read()).booleanValue());
        select(0);
        send("login", "testUser", "testPassword");
        assertTrue(Boolean.valueOf(read()).booleanValue());
        // Rimasti loggati 0, 1, 2, selezionato 0

    // TEST SULL'ELENCO VUOTO
      send("migliori");
      assertEquals("FINE", read());

  }

  @After
  public void exit() {
    try {
      for(int i=0; i<NUMBER_OF_TEST_SOCKETS; i++) {
        if (connected[i]){
          select(i);
          send("fine");
          socket[i].close();
          System.out.println("Disconnecting " + i);
        }
      }
    } catch (Exception e) {
        System.out.println("Exception: "+e);
        e.printStackTrace();
    }
  }

  // Wrappers per la gestione delle eccezioni della comunicazione.

  private void send(String message){
    try {
        outSocket[selected].println(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void send(String msg1, String msg2){
    send(msg1);
    send(msg2);
  }

  private void send(String msg1, String msg2, String msg3){
    send(msg1, msg2);
    send(msg3);
  }

  private String read() {
    String line = new String();
    try {
        line = inSocket[selected].readLine();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return line;
  }

  private void select(int i) {
    selected = i;
  }




}
