import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CharacterRecognition extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Style");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
