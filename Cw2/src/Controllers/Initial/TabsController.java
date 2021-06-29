package Controllers.Initial;

import Classes.Account.User;
import Controllers.Tabs.DoTestManagement.DoTestManagementController;
import Controllers.Tabs.DoTestManagement.ViewTestResultsController;
import Controllers.Tabs.TestManagement.TestManagementController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;

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
    private TestManagementController TestManagementController;

    @FXML
    private Tab tabViewTestResults;

    @FXML
    private ViewTestResultsController ViewTestResultsController;

    @FXML
    private Tab tabDoATest;

    @FXML
    private DoTestManagementController DoTestController;

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

        // Am only passing the currentUser to controllers that need it
        DoTestController.setCurrentUser(currentUser);   // The DoTestController would like to know the current user for saving results
        TestManagementController.setCurrentUser(currentUser);   // The TestManagementController wants to know it so it can show class options that only the user is in
        ViewTestResultsController.setCurrentUser(currentUser);  // The ViewTestResultsController wants to know so it can only show results related to the user

        // Hide all tabs
        tabsPaneMain.getTabs().clear();

        // Only show the ones that the account type is allowed to use
        switch (currentUser.getAccountType()) {
            case Student -> tabsPaneMain.getTabs().addAll(tabDoATest, tabViewTestResults);
            case Teacher -> tabsPaneMain.getTabs().addAll(tabClassManagement, tabQuestionManagement, tabTestManagement, tabViewTestResults);
            case SysAdmin -> tabsPaneMain.getTabs().addAll(tabUserManagement);
        }
    }
}