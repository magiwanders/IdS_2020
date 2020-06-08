package secondaprovainitinere.src;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;

public class AddArticleUI extends TwoFieldsWindow {

  public AddArticleUI(ClientUI owner) {
      super("Aggiungi Notizia", "Autore", "Titolo", "Aggiungi");
      super.setWidth(500);
      super.setHeight(200);
      super.initOwner((Stage)owner);
      super.show();
  }

  public String getAuthor() {
    return super.getTextField1();
  }

  public String getArticleTitle() {
    return super.getTextField2();
  }

  public Button getAddButton() {
    return super.getButton();
  }


}
