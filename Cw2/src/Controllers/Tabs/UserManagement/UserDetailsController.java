package Controllers.Tabs.UserManagement;

import Classes.Account.Student;
import Classes.Account.SysAdmin;
import Classes.Account.Teacher;
import Classes.Account.User;
import Classes.DataPersistence;
import Enums.AccountType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UserDetailsController implements Initializable {
    @FXML
    private Button buttonFinishUser;

    @FXML
    private ComboBox comboBoxUserTypeInput;

    @FXML
    private TextField textFieldFirstNameInput;

    @FXML
    private TextField textFieldLastNameInput;

    @FXML
    private TextField textFieldUsernameInput;

    @FXML
    private TextField textFieldPasswordInput;

    @FXML
    private DatePicker datePickerDOBInput;

    @FXML
    private HBox hBoxDOB;

    private ObservableList<User> usersObservableList;
    private UserManagementController.UserDetailsPurpose userDetailsPurpose;
    private User selectedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBoxItemsForInputs();
    }

    @FXML
    public void onFinishUserClick(ActionEvent event) {


        // The Gathering begins... (with some general validation)

        // Gather UserType value
        AccountType accountTypeInput = (AccountType) comboBoxUserTypeInput.getValue();
        if (accountTypeInput == null) {
            showIncompleteFormError();
            return;
        }

        // Gather First Name value
        String firstNameInput = textFieldFirstNameInput.getText();
        if (firstNameInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // Gather Last Name value
        String lastNameInput = textFieldLastNameInput.getText();
        if (lastNameInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // Gather Username value
        String usernameInput = textFieldUsernameInput.getText();
        if (usernameInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // Gather Password value
        String passwordInput = textFieldPasswordInput.getText();
        if (passwordInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // Gather DOB value with conditional validation since it is only needed for students
        LocalDate dateOfBirthInput = datePickerDOBInput.getValue();
        if (accountTypeInput == AccountType.Student && dateOfBirthInput == null) {
            showIncompleteFormError();
            return;
        }

        // The Gathering is complete


        // If a new user is being made OR if a username has been changed
        if (userDetailsPurpose == UserManagementController.UserDetailsPurpose.Add || !usernameInput.equals(selectedUser.getUsername())) {
            ObservableList<User> usersObservableList = FXCollections.observableArrayList();
            usersObservableList.addAll(DataPersistence.loadBank("userBank"));

            User someUser = usersObservableList.stream()
                    .filter(user -> usernameInput.equals((user).getUsername()))
                    .findFirst()
                    .orElse(null);

            if (someUser != null) { // If the username already exists, alert the user that they cannot use the username
                new Alert(Alert.AlertType.INFORMATION, "The username is already in use. Please use a different one.").show();
                return;
            }
        }

        // Generate the user in text-form in preparation to show to the user for confirmation
        String contentText = getContentText(accountTypeInput, firstNameInput, lastNameInput, usernameInput, passwordInput, dateOfBirthInput);

        // Do the assigned action with confirmation dialogs
        doActionWithConfirmation(accountTypeInput, firstNameInput, lastNameInput, usernameInput, passwordInput, dateOfBirthInput, contentText);

        // Closes this dialog now that the action has been finished with
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<User> _usersObservableList) {
        this.usersObservableList = _usersObservableList;
    }

    public void setUserDetailsPurpose(UserManagementController.UserDetailsPurpose _userDetailsPurpose) {
        this.userDetailsPurpose = _userDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (userDetailsPurpose) {
            case Add:
                buttonFinishUser.setText("Create User");
                // Must make the user select a user type first
                textFieldFirstNameInput.setDisable(true);
                textFieldLastNameInput.setDisable(true);
                textFieldUsernameInput.setDisable(true);
                textFieldPasswordInput.setDisable(true);
                datePickerDOBInput.setDisable(true);
                break;
            case Edit:
                buttonFinishUser.setText("Update User");
                // The user type cannot be changed once created, however its values still can
                comboBoxUserTypeInput.setDisable(true);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setSelectedUser(User _selectedUser) {
        this.selectedUser = _selectedUser;

        comboBoxUserTypeInput.setValue(_selectedUser.getAccountType());
        textFieldFirstNameInput.setText(_selectedUser.getFirstName());
        textFieldLastNameInput.setText(_selectedUser.getLastName());
        textFieldUsernameInput.setText(_selectedUser.getUsername());
        textFieldPasswordInput.setText(_selectedUser.getPassword());

        // Fill in the date, or just don't show the date field depending on if it is used
        if (selectedUser.getAccountType() == AccountType.Student) {
            datePickerDOBInput.setValue(((Student) _selectedUser).getDateOfBirth());
        }
        else {
            hBoxDOB.setVisible(false);
        }
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initComboBoxItemsForInputs() {
        // Inputting the userTypes into the comboBox

        ObservableList<AccountType> userTypes = FXCollections.observableArrayList();
        Arrays.stream(AccountType.values()).forEach(o -> userTypes.add(o));

        comboBoxUserTypeInput.setItems(userTypes);
    }

    @FXML
    public void onUserTypeSelect(ActionEvent event) {
        // If one of the inputs is disabled then, enable them now that a user type is selected
        if (textFieldFirstNameInput.isDisable()) {
            textFieldFirstNameInput.setDisable(false);
            textFieldLastNameInput.setDisable(false);
            textFieldUsernameInput.setDisable(false);
            textFieldPasswordInput.setDisable(false);
            datePickerDOBInput.setDisable(false);
        }

        // Clear the date when switching account types
        datePickerDOBInput.setValue(null);

        updateUIBasedOnNewAccountType((AccountType) comboBoxUserTypeInput.getValue());
    }

    public void updateUIBasedOnNewAccountType(AccountType accountType) {
        switch (accountType) {
            case Student -> {
                hBoxDOB.setVisible(true);
            }
            case Teacher, SysAdmin -> {
                hBoxDOB.setVisible(false);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public String getContentText(AccountType accountTypeInput, String firstNameInput, String lastNameInput, String usernameInput, String passwordInput, LocalDate dateOfBirthInput) {
        String contentText;

        switch (accountTypeInput) {
            case Student -> contentText =
                    "Account Type: : " + accountTypeInput +
                            "\nFirst Name: " + firstNameInput +
                            "\nLast Name: " + lastNameInput +
                            "\nUsername: " + usernameInput +
                            "\nPassword: " + passwordInput +
                            "\nDate of Birth: " + dateOfBirthInput;
            case Teacher, SysAdmin -> contentText =
                    "Account Type: : " + accountTypeInput +
                            "\nFirst Name: " + firstNameInput +
                            "\nLast Name: " + lastNameInput +
                            "\nUsername: " + usernameInput +
                            "\nPassword: " + passwordInput;
            default -> throw new IllegalArgumentException();
        }
        return contentText;
    }

    // 'Action' meaning the value in the userDetailsPurpose enum
    public void doActionWithConfirmation(AccountType accountTypeInput, String firstNameInput, String lastNameInput, String usernameInput, String passwordInput, LocalDate dateOfBirthInput, String contentText) {
        // Ask user if they are sure of their inputs
        Dialog<ButtonType> confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure?");
        confirmationDialog.setContentText(contentText);


        switch (userDetailsPurpose) {
            case Add -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    switch (accountTypeInput) {
                        case Student -> {
                            // Load the gathered inputs into the constructor
                            Student newStudent = new Student(firstNameInput, lastNameInput, usernameInput, passwordInput, dateOfBirthInput);
                            // Add the new constructed Student to the list
                            usersObservableList.add(newStudent);
                        }
                        case Teacher -> {
                            // Load the gathered inputs into the constructor
                            Teacher newTeacher = new Teacher(firstNameInput, lastNameInput, usernameInput, passwordInput);
                            // Add the new constructed Teacher to the list
                            usersObservableList.add(newTeacher);
                        }
                        case SysAdmin -> {
                            // Load the gathered inputs into the constructor
                            SysAdmin newSysAdmin = new SysAdmin(firstNameInput, lastNameInput, usernameInput, passwordInput);
                            // Add the new constructed SysAdmin to the list
                            usersObservableList.add(newSysAdmin);
                        }
                    }

                    new Alert(Alert.AlertType.CONFIRMATION, "The user is added to the user bank. Save the user bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("userBank", usersObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The user was not added.").show();
                }
            });
            case Edit -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    switch (accountTypeInput) {
                        case Student -> {
                            // Update the user data with the newly edited values
                            selectedUser.setFirstName(firstNameInput);
                            selectedUser.setLastName(lastNameInput);
                            selectedUser.setUsername(usernameInput);
                            selectedUser.setPassword(passwordInput);
                            ((Student)selectedUser).setDateOfBirth(dateOfBirthInput);
                        }
                        case Teacher, SysAdmin -> {
                            // Update the user data with the newly edited values
                            selectedUser.setFirstName(firstNameInput);
                            selectedUser.setLastName(lastNameInput);
                            selectedUser.setUsername(usernameInput);
                            selectedUser.setPassword(passwordInput);
                        }
                    }

                    new Alert(Alert.AlertType.CONFIRMATION, "The user is edited. Save the user bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("userBank", usersObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The user was not edited.").show();
                }
            });
            default -> throw new IllegalArgumentException();
        }
    }
}
