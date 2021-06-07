import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        //Configuring properties
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Views/Initial/Login.fxml"));
        }
        catch(IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load login screen").show();
        }
        primaryStage.setTitle("Cw2");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1000, 600));

        //Important configuration: Closes all child windows as well if the main window is closed
        primaryStage.setOnHidden(e -> Platform.exit());

        // Show main window
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
