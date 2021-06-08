package Controllers.Initial;

import Classes.Account.Student;
import Classes.Banks;
import Classes.Translating;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class LoginController implements Initializable {

    @FXML
    private TextField textFieldAccountNumber;

    @FXML
    private Button buttonSignIn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Banks.generateBanksIfNotFound();    // Simply generates all missing banks (as in questionBank, testBank, resultBank)
    }

    @FXML
    public void onEnter(ActionEvent event) {
        SignIn(textFieldAccountNumber.getText());
    }

    @FXML
    public void onClick(ActionEvent event) {


        Student student = new Student("abc", "last", Date.from(Instant.now()));
        System.out.println(student.getAccountType());



        SignIn(textFieldAccountNumber.getText());
    }

    public void SignIn(String accountNumber) {
        if (isSignInSuccessful(accountNumber)) {
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

            textFieldAccountNumber.requestFocus();
        }

    }

    public Boolean isSignInSuccessful(String accountNumber) {
        return true;    // Always return true for now, until Cw2.
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
