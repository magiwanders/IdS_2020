/*
 export PATH_TO_FX=secondaprovainitinere/src/javafx/lib
 javac --module-path $PATH_TO_FX --add-modules javafx.controls secondaprovainitinere/src/Client.java
 java --module-path $PATH_TO_FX --add-modules javafx.controls secondaprovainitinere.src.Client
*/
package secondaprovainitinere.src;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.* ;

import java.net.*;
import java.io.*;


public class Client extends Application {

    private final static int PORT=3000;
    private final static String address="localhost";

    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;

    private LoginUI loginUI;
    private ClientUI clientUI;
    private AddArticleUI addArticleUI;
    private AddCommentUI addCommentUI;

    private void connect() {
        try
        {
            socket = new Socket(address, PORT);
            //canali di comunicazione
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

            System.out.println("Connection esablished.");
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

    private void disconnect() {
      sendToServer("fine");
        try
        {
            socket.close();
            System.out.println("Socket closed.");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    private boolean loginAttempt(String usr, String psw) {
      Boolean loggato = false;
      if (fieldsNotEmpty(usr, psw)) {
        sendToServer("login", usr, psw);
        loggato=Boolean.valueOf(readLineFromServer()).booleanValue();
      }
      return loggato;
    }

    private boolean fieldsNotEmpty(String a, String b) {
      return (!a.isEmpty() && !b.isEmpty());
    }

    private void launchClientUI() {
      clientUI = new ClientUI(); // Finestra client.
      showNewsList();
      clientUI.getLogoutButton().setOnAction( (e1) -> logoutButtonHandle());
      clientUI.getAddArticleButton().setOnAction( (e2) -> addArticleButtonHandle());
      clientUI.getTopTenButton().setOnAction( (e3) -> topTenButtonHandle());
      clientUI.getShowListButton().setOnAction( (e4) -> showListButtonHandle());
      clientUI.getList().setOnMouseClicked( (e5) -> listHandle());
      clientUI.getAddCommentButton().setOnAction( (e6) -> addCommentHandle());
    }

    private void refreshAll() {
      showNewsList("no", clientUI.getSelectedArticle(), true, true);
    }

    private void showNewsList() {
      showNewsList("no", 1, true, true);
    }

    private void showNewsList(String wantToComment, int whatArticle, boolean REFRESH_ARTICLES, boolean REFRESH_COMMENTS) {
      if(wantToComment.equals("no")) showNewsList(wantToComment, whatArticle, REFRESH_ARTICLES, REFRESH_COMMENTS, "", "");
    }

    private void showNewsList(String wantToComment, int whatArticle, boolean REFRESH_ARTICLES, boolean REFRESH_COMMENTS, String vote, String comment) {
      sendToServer("discuti");
      retrieveArticles(REFRESH_ARTICLES);
      retrieveComments(whatArticle, REFRESH_COMMENTS);
      sendToServer(wantToComment);
      if(wantToComment.equals("si")) sendToServer(vote, comment);
      clientUI.showEdit();
    }

    private void retrieveArticles(boolean REFRESH_ARTICLES) {
      if(REFRESH_ARTICLES) clientUI.clearNews();
      int numberOfDiscussions = Integer.parseInt(readLineFromServer());

      for(int i=0;i<numberOfDiscussions;i++) {
          String article = readLineFromServer();
          if(REFRESH_ARTICLES) clientUI.addArticle(article);
      }
    }

    private void retrieveComments(int article, boolean REFRESH_COMMENTS) {
      if(REFRESH_COMMENTS) clientUI.clearComments();
      sendToServer(Integer.toString(article));
      int numberOfComments = Integer.parseInt(readLineFromServer());

      for(int i=0;i<numberOfComments;i++) {
          String vote = readLineFromServer();
          String comment = readLineFromServer();
          if(REFRESH_COMMENTS) clientUI.addComment(vote +" "+ comment);
      }

      if(REFRESH_COMMENTS) clientUI.showAddCommentButton();
    }

    private void showComments(int article) {
      showNewsList("no", article, false, true);
      clientUI.setSelected(article);
    }

    // Wrappers per la gestione delle eccezioni della comunicazione.

    private void sendToServer(String message){
      try {
          System.out.println("Inviato al server: " + message);
          outSocket.println(message);
          //outSocket.flush();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    private void sendToServer(String msg1, String msg2){
      sendToServer(msg1);
      sendToServer(msg2);
    }

    private void sendToServer(String msg1, String msg2, String msg3){
      sendToServer(msg1, msg2);
      sendToServer(msg3);
    }

    private String readLineFromServer() {
      String line = new String();
      try {
          line = inSocket.readLine();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return line;
    }


    // HANDLERS

    private void loginButtonHandle() {
      if(loginAttempt(loginUI.getUser(), loginUI.getPassword())) {
          loginUI.close();
          launchClientUI();
      } else loginUI.getErrorLabel().setText("Login Error.");
    }

    private void logoutButtonHandle() {
      clientUI.close();
      Platform.exit();
    }

    private void addArticleButtonHandle() {
      addArticleUI = new AddArticleUI(clientUI);
      addArticleUI.getAddButton().setOnAction( (e) -> addButtonHandle());
    }

    private void addButtonHandle() {
      String autore = addArticleUI.getAuthor();
      String titolo = addArticleUI.getArticleTitle();
      if (fieldsNotEmpty(autore, titolo)) {
        sendToServer("aggiungi", autore, titolo);
        addArticleUI.close();
        clientUI.setSelected(clientUI.getLastArticle()+1);
        showNewsList("no", clientUI.getLastArticle()+1, true, true);
      } else addArticleUI.getErrorLabel().setText("Author or title left blank.");
    }

    private void topTenButtonHandle() {
      sendToServer("migliori");
      clientUI.clearNews();
      for(int i=0;i<10;i++) {
          String article = readLineFromServer();
          if(article.equals("FINE")) break;
          clientUI.addArticle(article);
      }
      clientUI.hideEdit();
    }

    private void showListButtonHandle() {
      showNewsList();
    }

    private void listHandle() {
      if(!clientUI.isDisplayingTopTen()) {
        int selected = clientUI.updateSelectedArticle();
        showComments(selected);
      }
    }

    private void addCommentHandle() {
      addCommentUI = new AddCommentUI(clientUI);
      addCommentUI.getLeaveButton().setOnAction( (e) -> addCommentButtonHandle());
    }

    private void addCommentButtonHandle() {
      String voto = addCommentUI.getVote();
      String commento = addCommentUI.getBody();
      if(fieldsNotEmpty(voto, commento) && validVote(voto)) {
        showNewsList("si", clientUI.getSelectedArticle(), false, false, voto, commento);
        addCommentUI.close();
        refreshAll();
      } else addCommentUI.getErrorLabel().setText("One field left blank or vote not between 1 and 10.");
    }

    private boolean validVote(String vote) {
      try {
        int voteNumber = Integer.parseInt(vote);
        return voteNumber>0 && voteNumber<=10 ;
      } catch (Exception e) {
        return false;
      }
    }

    // Application specific methods and main.

    @Override
    public void init() throws Exception {
      System.out.println("Client is initializing.");
      connect();
    }

    @Override
    public void start(Stage loginWindow) throws Exception {
      loginUI = new LoginUI(); // Finestra di login.
      loginUI.getLoginButton().setOnAction( (e) -> loginButtonHandle() ); // Unica interazione possibile finestra di login.
    }

    @Override
    public void stop() throws Exception {
      //System.out.println("Client closing.");
      disconnect();
      System.out.println("Exiting.");

    }

    public static void main(String [] args) {
      launch();
    }

}
