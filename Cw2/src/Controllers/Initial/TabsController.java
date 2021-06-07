package Controllers.Initial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TabsController {

    @FXML
    private Button buttonLogout;

    @FXML

    // Issue with this event: All child windows should close or not work (when you execute something in it) at the very least once logged out
    public void onLogoutClick(ActionEvent event) {
        Stage stage = ((Stage)((Node)(event.getSource())).getScene().getWindow());
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("/Views/Initial/Login.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to logout").show();
        }
    }
}


