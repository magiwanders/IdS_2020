import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.* ;

public class LoginUI extends TwoFieldsWindow {

  public LoginUI() {
      super("Login", "Username", "Password", "Submit");
      super.show();
  }

  public String getUser() {
    return super.getTextField1();
  }

  public String getPassword() {
    return super.getTextField2();
  }

  public Label getErrorLabel() {
    return super.getErrorLabel();
  }

  public Button getLoginButton() {
    return super.getButton();
  }


}