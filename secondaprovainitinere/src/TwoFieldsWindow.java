package secondaprovainitinere;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.* ;

public class TwoFieldsWindow extends Stage {

  private VBox root;

    private Label label1;
    private TextField textField1;

    private Label label2;
    private TextField textField2;

    private Button button;

    private Label errorLabel;

  public TwoFieldsWindow(String title, String firstField, String secondField, String buttonText) {
      super();
      //scene.getStyleSheets().add("");
      root = new VBox();
      label1 = new Label(firstField);
      textField1 = new TextField();
      label2 = new Label(secondField);
      textField2 = new TextField();
      button = new Button(buttonText);
      errorLabel = new Label();

      assembleGUI();

      super.setTitle(title);
      super.setScene(new Scene(root));
  }

  private void assembleGUI() {
    root.getChildren().add(label1);
    root.getChildren().add(textField1);
    root.getChildren().add(label2);
    root.getChildren().add(textField2);
    button.setDefaultButton(true);
    root.getChildren().add(button);
    errorLabel.setWrapText(true);
    root.getChildren().add(errorLabel);
  }

  public String getTextField1() {
    return textField1.getText();
  }

  public String getTextField2() {
    return textField2.getText();
  }

  public Label getErrorLabel() {
    return errorLabel;
  }

  public Button getButton() {
    return button;
  }

}
