package Controllers.Initial;

import Classes.Account.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TabsController {

    @FXML
    private Button buttonLogout;

    private User currentUser;

    @FXML
    private TabPane tabsPaneMain;

    @FXML
    private Tab tabQuestionManagement;

    @FXML
    private Tab tabTestManagement;

    @FXML
    private Tab tabViewTestResults;

    @FXML
    private Tab tabDoATest;

    @FXML
    private Tab tabUserManagement;

    @FXML
    private Tab tabClassManagement;


    @FXML
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

    public void setCurrentUser(User _currentUser) {
        currentUser = _currentUser;

        tabsPaneMain.getTabs().clear();

        switch (currentUser.getAccountType()) {
            case Student -> tabsPaneMain.getTabs().addAll(tabDoATest, tabViewTestResults);
            case Teacher -> tabsPaneMain.getTabs().addAll(tabClassManagement, tabQuestionManagement, tabTestManagement, tabViewTestResults);
            case SysAdmin -> tabsPaneMain.getTabs().addAll(tabUserManagement);
        }
    }
}


