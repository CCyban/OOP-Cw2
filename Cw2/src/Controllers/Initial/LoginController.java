package Controllers.Initial;

import Classes.Account.User;
import Classes.Banks;
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
        Banks.generateBanksIfNotFound();    // Simply generates all missing banks (as in questionBank, testBank, resultBank, userBank)
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
        if (isSignInSuccessful(usernameInput, passwordInput)) {
            Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
            alertSignIn.setTitle("Sign In Attempt");
            alertSignIn.setHeaderText("Sign In Attempt Successful");
            alertSignIn.showAndWait();

            updateStageOnSuccessfulSignIn();
        }
        else
        {
            Dialog<ButtonType> alertSignIn = new Alert(Alert.AlertType.INFORMATION);
            alertSignIn.setTitle("Sign In Attempt");
            alertSignIn.setHeaderText("Sign In Attempt Failed");
            alertSignIn.showAndWait();

            textFieldUsernameInput.requestFocus();
        }

    }

    public Boolean isSignInSuccessful(String usernameInput, String passwordInput) {

        ObservableList<User> usersObservableList = FXCollections.observableArrayList();
        Banks.loadUserBank(false, true, usersObservableList);

        User someUser = (usersObservableList.stream()
                .filter(user -> usernameInput.equals((user).getUsername()))
                .findFirst()
                .orElse(null));

        if (someUser != null) { // If the username already exists, check if the password is correct
            if (someUser.getPassword().equals(passwordInput)) {
                return true;
            }
        }
        return false;
    }

    public void updateStageOnSuccessfulSignIn() {
        Stage stage = (Stage) buttonSignIn.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Initial/Tabs.fxml"));
            stage.setScene(new Scene(root, 1000, 600));
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load the tabs").show();
        }
    }
}
