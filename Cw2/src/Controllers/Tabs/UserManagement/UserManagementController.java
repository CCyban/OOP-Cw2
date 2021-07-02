package Controllers.Tabs.UserManagement;

import Classes.Account.User;
import Classes.Class;
import Classes.DataPersistence;
import Classes.Quiz.Result;
import Classes.Quiz.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class UserManagementController implements Initializable {
    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewUsers;

    private ObservableList<User> usersObservableList = FXCollections.observableArrayList();

    public enum UserDetailsPurpose { Add, Edit };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a user contains the string from the search (purposely not case-sensitive)
            Predicate<User> predicateContainsNonCaseStringOnly = q -> (q.getFullName().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewUsers.setItems(usersObservableList.filtered(predicateContainsNonCaseStringOnly));

        });

        // Load all stored users into an ObservableList
        usersObservableList.clear();
        usersObservableList.addAll(DataPersistence.loadBank("userBank"));

        // Load TableView with its columns & the newly made ObservableList
        initTableViewUsers();
    }

    public void initTableViewUsers() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("User Id");
        idCol.setPrefWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<User, UUID>("userUUID"));

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setPrefWidth(110);
        typeCol.setCellValueFactory(new PropertyValueFactory<User, Enums.AccountType>("accountType"));

        TableColumn nameCol = new TableColumn("Full Name");
        nameCol.setPrefWidth(530);
        nameCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("fullName"));


        // Add the constructed columns to the TableView
        tableViewUsers.getColumns().addAll(idCol, typeCol, nameCol);

        // Hook up the observable list with the TableView
        tableViewUsers.setItems(usersObservableList);
    }

    @FXML
    public void onAddNewUserClick(ActionEvent event) {
        openUserDetails(UserDetailsPurpose.Add);
    }

    @FXML
    public void onEditSelectedUserClick(ActionEvent event) {
        openUserDetails(UserDetailsPurpose.Edit);
    }

    @FXML
    public void onRemoveSelectedUserClick(ActionEvent event) {
        // If a user is not selected then the action cannot proceed
        if (tableViewUsers.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No user is selected with your action").show();
            return;
        }


        User selectedUser = ((User) tableViewUsers.getSelectionModel().getSelectedItem());

        // If the user has a result dependency, do not allow deletion
        ObservableList<Result> resultBank = FXCollections.observableArrayList(DataPersistence.loadBank("resultBank"));

        for (Result result: resultBank) { // For every result in the resultBank
            UUID resultUserUUID = result.getUserUUID();   // Get the user UUID of the result

            // Am using .toString() on the UUIDs because they will never match otherwise due to how the UUID type works
            if (resultUserUUID.toString().equals(selectedUser.getUserUUID().toString())) { // If it matches then alert the user that it has an existing usage so it cannot be deleted just yet.
                new Alert(Alert.AlertType.ERROR, "This user is used in at least one result, therefore it cannot be deleted. Delete the related results to delete this.").show();
                return;
            }
        }

        // If the user has a class dependency, do not allow deletion
        ObservableList<Class> classBank = FXCollections.observableArrayList(DataPersistence.loadBank("classBank"));

        for (Class aClass: classBank) { // For every class in the classBank
            List<UUID> resultUserUUIDsList = aClass.getUserUUIDs();   // Get the user UUID of the class

            for (UUID userUUID: resultUserUUIDsList) {    // For every user in a class, check if it contains a user that this user wishes to delete
                if (userUUID.toString().equals(selectedUser.getUserUUID().toString())) {
                    new Alert(Alert.AlertType.ERROR, "This user is used in at least one class, therefore it cannot be deleted. Separate any classes from this user first to delete this.").show();
                    return;
                }
            }
        }

        // Now that we know a user is selected & it has no result dependencies, it can be deleted safely
        usersObservableList.remove(
                tableViewUsers.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the usersObservableList
        new Alert(Alert.AlertType.INFORMATION, "User Deleted").show();
    }

    public void openUserDetails(UserDetailsPurpose userDetailsPurpose) {
        // If a user is not selected then the action cannot proceed (unless the user is adding an user, which doesn't require any selected user)
        if (tableViewUsers.getSelectionModel().getSelectedItem() == null && userDetailsPurpose != UserDetailsPurpose.Add) {
            new Alert(Alert.AlertType.ERROR, "No user is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/UserManagement/UserDetails.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Failed to load the UserDetails dialog").show();
        }

        Scene scene = new Scene(parent, 900, 500);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        UserDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(usersObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (userDetailsPurpose) {
            case Add -> {
                stage.setTitle("Add New User");
                dialogController.setUserDetailsPurpose(UserDetailsPurpose.Add);
            }
            case Edit -> {
                stage.setTitle("Edit Selected User");
                dialogController.setUserDetailsPurpose(userDetailsPurpose.Edit);
                dialogController.setSelectedUser((User) tableViewUsers.getSelectionModel().getSelectedItem());
            }
            default -> throw new IllegalArgumentException();
        }

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();

        if (userDetailsPurpose == userDetailsPurpose.Edit) {
            tableViewUsers.refresh();   // Updates the TableView so it can show the latest version of an edited user
            // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.

            // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
            // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
        }
    }

    @FXML
    public void onLoadUsersClick() {
        usersObservableList.clear();
        usersObservableList.addAll(DataPersistence.loadBank("userBank"));
    }

    @FXML
    public void onSaveUsersClick() {
        DataPersistence.saveBank("userBank", usersObservableList.stream().toList());
    }
}
