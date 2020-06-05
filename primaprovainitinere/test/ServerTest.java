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
    // TEST DI LOGIN

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
      // Lista di discussioni vuota ma scelgo di commentare
      send("discuti");
      assertEquals("0", read());
      send("1");
      assertEquals("0", read());
      send("si", "6", "commentopersonelvuoto");
      // TopTen vuoto
      send("migliori");
      assertEquals("FINE", read());
      // Lista di dicussioni vuota
      send("discuti");
      assertEquals("0", read());
      // Per specifica bisogna scegliere comunque una discussione fittizia, verifico che i commenti siano comunque 0
      send("1");
      assertEquals("0", read());
      send("no");

    // TEST SULL'AGGIUNTA DI NOTIZIE
      send("aggiungi", "Primo Levi", "Se questa è una notizia");
      select(1);
      send("migliori");
      assertEquals("Voto: NaN | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("FINE", read());
      send("aggiungi", "Giovanni Paolo II", "Beati... i secondi");

    // TEST SULL"AGGIUNTA DI COMMENTI
      // Aggiunta commento semplice e visualizzazione di notizie multiple
      select(2);
      send("discuti");
      assertEquals("2", read());
      assertEquals("[N^1] Voto: NaN | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("[N^2] Voto: NaN | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      send("1");
      assertEquals("0", read());
      send("si", "8", "Buona parodia");
      // Visualizzazione commento
      select(1);
      send("discuti");
      assertEquals("2", read());
      assertEquals("[N^1] Voto: 8.0 | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("[N^2] Voto: NaN | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      send("1");
      assertEquals("1", read());
      assertEquals("8", read());
      assertEquals("[testUser2] Buona parodia", read());
      send("si", "9", "Pienamente d'accordo");
      // Visualizzazione commenti multipli
      select(0);
      send("discuti");
      assertEquals("2", read());
      assertEquals("[N^1] Voto: 8.5 | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("[N^2] Voto: NaN | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      send("1");
      assertEquals("2", read());
      assertEquals("8", read());
      assertEquals("[testUser2] Buona parodia", read());
      assertEquals("9", read());
      assertEquals("[testUser1] Pienamente d'accordo", read());
      send("no");

    // TEST TOP TEN
      send("migliori");
      assertEquals("Voto: 8.5 | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("Voto: NaN | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      assertEquals("FINE", read());
      send("discuti");
      assertEquals("2", read());
      assertEquals("[N^1] Voto: 8.5 | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("[N^2] Voto: NaN | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      send("2");
      assertEquals("0", read());
      send("si", "9", "Santo padre, ha sbagliato citazione");
      select(1);
      send("discuti");
      assertEquals("2", read());
      assertEquals("[N^1] Voto: 8.5 | Di: Primo Levi. 'Se questa è una notizia'", read());
      assertEquals("[N^2] Voto: 9.0 | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      send("2");
      assertEquals("1", read());
      assertEquals("9", read());
      assertEquals("[testUser] Santo padre, ha sbagliato citazione", read());
      send("no");
      send("migliori");
      assertEquals("Voto: 9.0 | Di: Giovanni Paolo II. 'Beati... i secondi'", read());
      assertEquals("Voto: 8.5 | Di: Primo Levi. 'Se questa è una notizia'", read());
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
        try { // Affinchè i messaggi siano distanziati di almeno 15 millisecondi (per permettere al server di eseguire tutte le operazioni)
          Thread.sleep(15);
        } catch(Exception e) {
          System.out.println("Interrupted");
        }
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
