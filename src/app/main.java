package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("/app/login/login_menu.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML not found");
        }
        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Topsis Bencana");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);

        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
