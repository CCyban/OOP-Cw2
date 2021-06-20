package Controllers.Tabs.ClassManagement;

import Classes.Account.User;
import Classes.Class;
import Classes.DataPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class ClassDetailsController implements Initializable {
    @FXML
    private Button buttonFinishClass;

    @FXML
    private TextField textFieldYearGroupInput;

    @FXML
    private TextField textFieldSubjectInput;

    @FXML
    private TableView<User> tableViewUserBank;

    @FXML
    private TableView<User> tableViewClassUsers;

    private ObservableList<Class> classesObservableList;
    private ClassManagementController.ClassDetailsPurpose classDetailsPurpose;
    private Class selectedClass;

    private ObservableList<User> userBankObservableList = FXCollections.observableArrayList();
    private ObservableList<User> classUsersObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTextFormattersForInputs();

        userBankObservableList.clear();
        userBankObservableList.addAll(DataPersistence.loadBank("userBank"));

        initUserTableView(tableViewUserBank, userBankObservableList);
        initUserTableView(tableViewClassUsers, classUsersObservableList);
    }

    @FXML
    public void onFinishClassClick(ActionEvent event) {


        // The Gathering begins... (with some general validation)

        // Gather Year Group value
        String yearGroupInput = textFieldYearGroupInput.getText();
        if (yearGroupInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // Gather Subject value
        String subjectInput = textFieldSubjectInput.getText();
        if (subjectInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // The Gathering is complete

        // Generate the class in text-form in preparation to show to the user for confirmation
        String contentText = getContentText(yearGroupInput, subjectInput, classUsersObservableList.size());

        // Do the assigned action with confirmation dialogs
        doActionWithConfirmation(yearGroupInput, subjectInput, contentText);

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
                buttonFinishClass.setText("Create Class");
                break;
            case Edit:
                buttonFinishClass.setText("Update Class");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setSelectedClass(Class _selectedClass) {
        this.selectedClass = _selectedClass;

        this.classUsersObservableList = FXCollections.observableArrayList(selectedClass.getUsers());

        textFieldYearGroupInput.setText(_selectedClass.getYearGroup());
        textFieldSubjectInput.setText(_selectedClass.getSubject());

        this.classUsersObservableList = FXCollections.observableArrayList(selectedClass.getUsers());
        tableViewClassUsers.setItems(classUsersObservableList);
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initTextFormattersForInputs() {
        // TODO: Maybe?
    }

    public String getContentText(String yearGroupInput, String subjectInput, int usersCount) {
        String contentText =
                "Year Group: " + yearGroupInput +
                        "\nSubject: " + subjectInput +
                        "\nAmount of users in Class: " + usersCount;
        return contentText;
    }

    // 'Action' meaning the value in the userDetailsPurpose enum
    public void doActionWithConfirmation(String yearGroupInput, String subjectInput, String contentText) {

        // Ask user if they are sure of their inputs
        Dialog<ButtonType> confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure?");
        confirmationDialog.setContentText(contentText);


        switch (classDetailsPurpose) {
            case Add -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    selectedClass.setYearGroup(yearGroupInput);
                    selectedClass.setSubject(subjectInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The class is added to the class bank. Save the class bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("classBank", classesObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The class was not added.").show();
                }
            });
            case Edit -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    selectedClass.setYearGroup(yearGroupInput);
                    selectedClass.setSubject(subjectInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The class is edited. Save the class bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("classBank", classesObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The class was not edited.").show();
                }
            });
            default -> throw new IllegalArgumentException();
        }


    }

    @FXML
    private void onAddToClassClick() {
        // If a user is not selected then the action cannot proceed
        if (tableViewUserBank.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No user is selected with your action").show();
            return;
        }

        // If a user is already added then the action should not proceed
        if (tableViewClassUsers.getItems().stream()
                .filter(user -> tableViewUserBank.getSelectionModel().getSelectedItem().getUserUUID().equals((user).getUserUUID()))
                .findFirst()
                .orElse(null) != null) {

            new Alert(Alert.AlertType.ERROR, "User is already added").show();
            return;
        }

        // Adds the user to the class
        selectedClass.addUser((tableViewUserBank.getSelectionModel().getSelectedItem()).getUserUUID());
        // Refreshes data/table
        classUsersObservableList = FXCollections.observableArrayList(selectedClass.getUsers());
        tableViewClassUsers.setItems(classUsersObservableList);

    }

    @FXML
    private void onRemoveFromClassClick() {
        // If a user is not selected then the action cannot proceed
        if (tableViewClassUsers.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No user is selected with your action").show();
            return;
        }

        // Removes the user from the class
        selectedClass.removeUser(((User) tableViewClassUsers.getSelectionModel().getSelectedItem()).getUserUUID());
        // Refreshes data/table
        classUsersObservableList = FXCollections.observableArrayList(selectedClass.getUsers());
        tableViewClassUsers.setItems(classUsersObservableList);
    }

    public void initUserTableView(TableView userTableViewToInit, ObservableList observableListToBind) {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("User Id");
        idCol.setCellValueFactory(new PropertyValueFactory<User, UUID>("userUUID"));
        idCol.setPrefWidth(100);

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<User, Enums.AccountType>("accountType"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("fullName"));


        // Add the constructed columns to the TableView
        userTableViewToInit.getColumns().addAll(idCol, typeCol, nameCol);

        // Hook up the observable list with the TableView
        userTableViewToInit.setItems(observableListToBind);
    }
}
