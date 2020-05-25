import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.* ;

public class ClientUI extends Stage {

  private VBox root;

    private HBox header;
      private Label title;
      private Label show;
      private VBox viewOptions;
        private ToggleGroup toggleOptions;
          private RadioButton showList;
          private RadioButton showTopTen;
      private Button logout;

    private Button addArticle;

    private HBox commentPanel;
      private Label commentTitle;
      private Label selectedArticle;

    private ListView<String> listContainer;
    private ScrollPane commentContainer;
      private VBox comments;

      private Button addComment;


  public ClientUI() {
    super();

    root = new VBox();
      header = new HBox();
        title = new Label("Lista di Notizie");
        show = new Label("Mostra: ");
        viewOptions = new VBox();
          toggleOptions = new ToggleGroup();
            showList = new RadioButton("Lista completa");
            showTopTen = new RadioButton("Top 10");
        logout = new Button("Logout & Exit");

      addArticle = new Button("+ Aggiungi Notizia");

      commentPanel = new HBox();
        commentTitle = new Label("Commenti della notizia ");
        selectedArticle = new Label("1");

      listContainer = new ListView<String>();

      commentContainer = new ScrollPane();
        comments = new VBox();

      addComment = new Button("+ Lascia Commento");

    assembleGUI();

    super.setScene(new Scene(root));
    super.setTitle("Client");
    super.setWidth(1000);
    super.setHeight(700);
    super.show();
  }

  private void assembleGUI() {
      root.getChildren().add(header);
        header.getChildren().add(title);
        header.getChildren().add(show);
        header.getChildren().add(viewOptions);
          viewOptions.getChildren().add(showList);
          showList.setToggleGroup(toggleOptions);
          showList.setSelected(true);
          viewOptions.getChildren().add(showTopTen);
          showTopTen.setToggleGroup(toggleOptions);

      header.getChildren().add(addArticle);

      header.getChildren().add(logout);

      root.getChildren().add(listContainer);
      listContainer.setMinHeight(300);

      root.getChildren().add(commentPanel);
        commentPanel.getChildren().add(commentTitle);
        commentPanel.getChildren().add(selectedArticle);

      root.getChildren().add(commentContainer);
      commentContainer.setContent(comments);

      root.getChildren().add(addComment);

      addComment.setVisible(false);
      commentContainer.setMinHeight(300);
  }

  public Button getAddArticleButton() {
    return addArticle;
  }

  public void addArticle(String content) {
    listContainer.getItems().add(content);
  }

  public void addComment(String comment) {
    comments.getChildren().add(new Label(comment));
  }

  public void showAddCommentButton() {
    comments.getChildren().add(addComment);
    addComment.setVisible(true);
  }

  public Button getLogoutButton() {
    return logout;
  }

  public RadioButton getTopTenButton() {
    return showTopTen;
  }

  public RadioButton getShowListButton() {
    return showList;
  }

  public Button getAddButton() {
    return addArticle;
  }

  public Button getAddCommentButton() {
    return addComment;
  }

  public int getLastArticle() {
    return listContainer.getItems().size();
  }

  public void clearNews() {
    listContainer.getItems().clear();
  }

  public void clearComments() {
    comments.getChildren().clear();
  }

  public ListView getList() {
    return listContainer;
  }

  public int updateSelectedArticle() {
    try {
      String current = listContainer.getSelectionModel().getSelectedItem();
      return indexOf(current);
    } catch (Exception e) {
      selectedArticle.setText("1");
      return 1;
    }
  }

  public int getSelectedArticle() {
    return Integer.parseInt(selectedArticle.getText());
  }

  public void setSelectedArticle(int n) {
    selectedArticle.setText(Integer.toString(n));
  }

  public int indexOf(String current) {
    return Integer.parseInt(current.substring(current.indexOf("^")+1, current.indexOf("]")));
  }

  public void setSelected(int selected) {
    selectedArticle.setText(Integer.toString(selected));
  }

  public void showEdit() {
    addArticle.setVisible(true);
    commentPanel.setVisible(true);
    commentContainer.setVisible(true);
  }

  public void hideEdit() {
    addArticle.setVisible(false);
    commentPanel.setVisible(false);
    commentContainer.setVisible(false);
    selectedArticle.setText("1");
  }

  public boolean isDisplayingTopTen() {
    return showTopTen.isSelected();
  }

}
