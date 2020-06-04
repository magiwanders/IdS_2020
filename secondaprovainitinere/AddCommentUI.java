package secondaprovainitinere;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;

public class AddCommentUI extends TwoFieldsWindow {

  public AddCommentUI(ClientUI owner) {
      super("Add Comment", "Voto", "Commento", "Invia");
      super.setWidth(500);
      super.setHeight(200);
      super.initOwner((Stage)owner);
      super.show();
  }

  public String getVote() {
    return super.getTextField1();
  }

  public String getBody() {
    return super.getTextField2();
  }

  public Label getErrorLabel() {
    return super.getErrorLabel();
  }

  public Button getLeaveButton() {
    return super.getButton();
  }


}
