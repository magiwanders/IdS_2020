/*import javafx.graphics;

public class grafica extends Application {

    @Override public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 100, 100);
        stage.setScene(scene);

        Circle c1 = new Circle(50.0f, 50.0f, 50.0f, Color.RED);

        root.getChildren().add(c1);
        stage.setVisible(true);
    }

    public static void main(String [] args) {
        Launcher.launch(grafica.class, null);
    }
}*/

//package helloworld;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class grafica extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");


        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });


        StackPane root = new StackPane();
        root.getChildren().add(btn);

 Scene scene = new Scene(root, 500, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 public static void main(String[] args) {
        launch(args);
    }
}
