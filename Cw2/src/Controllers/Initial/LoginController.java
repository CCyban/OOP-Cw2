package Controllers.Initial;

import Classes.Account.User;
import Classes.DataPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField textFieldUsernameInput;

    @FXML
    private PasswordField passwordFieldPasswordInput;

    @FXML
    private Button buttonSignIn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //List test = DataPersistence.loadBank("questionBank");

/*
        ObservableList obl = FXCollections.observableArrayList();
        Banks.loadQuestionBank(false, false, obl);
        DataPersistence.saveBank("questionBank", obl.stream().toList());
 */

        DataPersistence.defaultDataIfNoAdminExists(); // Simply generates a default admin if one doesn't exist
    }

    @FXML
    public void onEnter(ActionEvent event) {
        SignIn(textFieldUsernameInput.getText(), passwordFieldPasswordInput.getText());
    }

    @FXML
    public void onClick(ActionEvent event) {
        SignIn(textFieldUsernameInput.getText(), passwordFieldPasswordInput.getText());
    }

    public void SignIn(String usernameInput, String passwordInput) {
        User currentUser = getUser(usernameInput, passwordInput);

        Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
        alertSignIn.setTitle("Sign In Attempt");

        if (currentUser != null) {
            alertSignIn.setHeaderText("Sign In Attempt Successful");
            alertSignIn.showAndWait();

            updateStageOnSuccessfulSignIn(currentUser);
        }
        else
        {
            alertSignIn.setHeaderText("Sign In Attempt Failed");
            alertSignIn.showAndWait();

            textFieldUsernameInput.requestFocus();
        }

    }

    public User getUser(String usernameInput, String passwordInput) {

        ObservableList<User> usersObservableList = FXCollections.observableArrayList();
        usersObservableList.addAll(DataPersistence.loadBank("userBank"));

        User someUser = (usersObservableList.stream()
                .filter(user -> usernameInput.equals((user).getUsername()))
                .findFirst()
                .orElse(null));

        if (someUser != null) { // If the username exists, check if the password is correct
            if (someUser.getPassword().equals(passwordInput)) {
                return someUser;
            }
        }
        return null;
    }

    public void updateStageOnSuccessfulSignIn(User currentUser) {
        Stage stage = (Stage) buttonSignIn.getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Initial/Tabs.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root, 1000, 600));

            TabsController tabsController = fxmlLoader.getController();
            tabsController.setCurrentUser(currentUser);
        }
        catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load the tabs").show();
        }
    }
}
