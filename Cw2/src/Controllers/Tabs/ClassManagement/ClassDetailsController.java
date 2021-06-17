package Controllers.Tabs.ClassManagement;

import Classes.Account.Student;
import Classes.Account.SysAdmin;
import Classes.Account.Teacher;
import Classes.Banks;
import Classes.Class;
import Enums.AccountType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ClassDetailsController implements Initializable {
    @FXML
    private Button buttonFinishClass;

    @FXML
    private TextField textFieldFirstNameInput;

    @FXML
    private TextField textFieldLastNameInput;

    @FXML
    private TextField textFieldUsernameInput;

    @FXML
    private TextField textFieldPasswordInput;

    private ObservableList<Class> classesObservableList;
    private ClassManagementController.ClassDetailsPurpose classDetailsPurpose;
    private Class selectedClass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTextFormattersForInputs();
    }

    @FXML
    public void onFinishClassClick(ActionEvent event) {


        // The Gathering begins... (with some general validation)

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

        // The Gathering is complete


        // TODO: Make the class-specific validation
        /*
        // If a new user is being made OR if a username has been changed
        if (userDetailsPurpose == ClassManagementController.UserDetailsPurpose.Add || usernameInput != selectedUser.getUsername()) {
            ObservableList<User> usersObservableList = FXCollections.observableArrayList();
            Banks.loadUserBank(false, true, usersObservableList);


            User someUser = (usersObservableList.stream()
                    .filter(user -> usernameInput.equals((user).getUsername()))
                    .findFirst()
                    .orElse(null));

            if (someUser != null) { // If the username already exists, alert the user that they cannot use the username
                new Alert(Alert.AlertType.INFORMATION, "The username is already in use. Please use a different one.").show();
                return;
            }
        }

         */

        // TODO: Validation for inputs
        // Question-type specific validation, am using switch statement here so new cases can easily be added whilst maintaining efficiency
        /*
        switch (accountTypeInput) {
            case MultiChoice:
                // Storing a local copy of the question string as it will be modified
                String entireQuestion = questionInput;

                if (!entireQuestion.contains("(") || !entireQuestion.contains(")")) {   //
                    new Alert(Alert.AlertType.INFORMATION, "The question needs to contain options.\nLook at the help text below the question text box for an example.").show();
                    return;
                }

                // Find the split of the subsections & remove the brackets to clean it up
                int optionsIndex = entireQuestion.indexOf('(');                     // Finds where initial question ends & the options begin
                entireQuestion = entireQuestion.replace("(", "");   // Removes the '('
                entireQuestion = entireQuestion.replace(")", "");   // Removes the ')'

                if (entireQuestion.length() + 2 != questionInput.length()) { // If the question contains more brackets than just a single pair, alert user to do it properly
                    new Alert(Alert.AlertType.ERROR, "The question contains more brackets than just a single pair for options.").show();
                    return;
                }

                // Get the 'options' subsection
                String questionOptionsString = entireQuestion.substring(optionsIndex);    // Splits the entireQuestion into the question options part
                List questionOptionsList = Arrays.asList(questionOptionsString.split("\\s*,\\s*"));

                if (questionOptionsList.size() > 4 ) { // If the question contains at least 1 option
                    new Alert(Alert.AlertType.ERROR, "There needs to be no more than 4 options in the question.").show();
                    return;
                }

                if (questionOptionsList.size() == 1 && questionOptionsList.get(0).equals("")) {
                    new Alert(Alert.AlertType.ERROR, "There needs to at least 1 option in the question.").show();
                    return;
                }


                if (!questionOptionsList.contains(answerInput)) {
                    new Alert(Alert.AlertType.INFORMATION, "One of the question options needs to be the answer").show();
                    return;
                }
                break;
            default: break; // It's okay if a question-type doesn't need any further validation than the general validation
        }

         */

        // Generate the user in text-form in preparation to show to the user for confirmation
        //String contentText = getContentText(firstNameInput, lastNameInput, usernameInput, passwordInput);

        // Do the assigned action with confirmation dialogs
        //doActionWithConfirmation(firstNameInput, lastNameInput, usernameInput, passwordInput, contentText);

        // Closes this dialog now that the action has been finished with
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Class> _classesObservableList) {
        this.classesObservableList = _classesObservableList;
    }

    public void setClassDetailsPurpose(ClassManagementController.ClassDetailsPurpose _classDetailsPurpose) {
        this.classDetailsPurpose = _classDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (classDetailsPurpose) {
            case Add:
                buttonFinishClass.setText("Create User");
                // Must make the user select a user type first
                textFieldFirstNameInput.setDisable(true);
                textFieldLastNameInput.setDisable(true);
                break;
            case Edit:
                buttonFinishClass.setText("Update User");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setSelectedClass(Class _selectedClass) {
        this.selectedClass = _selectedClass;

        /*  // TODO: map the data
        textFieldFirstNameInput.setText(_selectedUser.getFirstName());
        textFieldLastNameInput.setText(_selectedUser.getLastName());
        textFieldUsernameInput.setText(_selectedUser.getUsername());
        textFieldPasswordInput.setText(_selectedUser.getPassword());

         */
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initTextFormattersForInputs() {
        // TODO: Maybe?
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
        /*
        // Ask user if they are sure of their inputs
        Dialog<ButtonType> confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure?");
        confirmationDialog.setContentText(contentText);


        switch (classDetailsPurpose) {
            case Add -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    switch (accountTypeInput) {
                        case Student -> {
                            // Load the gathered inputs into the constructor
                            Student newStudent = new Student(firstNameInput, lastNameInput, usernameInput, passwordInput, dateOfBirthInput);
                            // Add the new constructed Student to the list
                            classesObservableList.add(newStudent);
                        }
                        case Teacher -> {
                            // Load the gathered inputs into the constructor
                            Teacher newTeacher = new Teacher(firstNameInput, lastNameInput, usernameInput, passwordInput);
                            // Add the new constructed Teacher to the list
                            classesObservableList.add(newTeacher);
                        }
                        case SysAdmin -> {
                            // Load the gathered inputs into the constructor
                            SysAdmin newSysAdmin = new SysAdmin(firstNameInput, lastNameInput, usernameInput, passwordInput);
                            // Add the new constructed SysAdmin to the list
                            classesObservableList.add(newSysAdmin);
                        }
                    }

                    new Alert(Alert.AlertType.CONFIRMATION, "The user is added to the user bank. Save the user bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            Banks.saveUserBank(true, true, classesObservableList);
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
                            selectedClass.setFirstName(firstNameInput);
                            selectedClass.setLastName(lastNameInput);
                            if (selectedClass.getAccountType() == AccountType.Student) {
                                ((Student) selectedClass).setDateOfBirth(dateOfBirthInput);
                            }
                        }
                        case Teacher, SysAdmin -> {
                            // Update the user data with the newly edited values
                            selectedClass.setFirstName(firstNameInput);
                            selectedClass.setLastName(lastNameInput);
                        }
                    }

                    new Alert(Alert.AlertType.CONFIRMATION, "The user is edited. Save the user bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            Banks.saveUserBank(true, true, classesObservableList);
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The user was not edited.").show();
                }
            });
            default -> throw new IllegalArgumentException();
        }

         */
    }
}
