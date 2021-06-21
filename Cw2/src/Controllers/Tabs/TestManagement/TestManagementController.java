package Controllers.Tabs.TestManagement;

import Classes.Account.User;
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
import java.util.*;
import java.util.function.Predicate;

public class TestManagementController implements Initializable {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewTests;

    private ObservableList<Test> testsObservableList = FXCollections.observableArrayList();

    private User currentUser;

    public enum TestDetailsPurpose { Add, Edit };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Test> predicateContainsNonCaseStringOnly = q -> (q.getTestTitle().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewTests.setItems(testsObservableList.filtered(predicateContainsNonCaseStringOnly));
        });

        // Load (if any) stored tests into an ObservableList
        testsObservableList.clear();
        testsObservableList.addAll(DataPersistence.loadBank("testBank"));

        // Load TableView with its columns & the newly made ObservableList
        initTableViewTests();
    }

    public void initTableViewTests() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Test Id");
        idCol.setPrefWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<Test, UUID>("testUUID"));

        /*
        TableColumn classIdCol = new TableColumn("Class Id");
        classIdCol.setPrefWidth(100);
        classIdCol.setCellValueFactory(new PropertyValueFactory<Test, UUID>("classUUID"));

         */

        TableColumn testTitleCol = new TableColumn("Test Title");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Test, String>("TestTitle"));

        TableColumn totalMarksCol = new TableColumn("Total Possible Marks");
        totalMarksCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("totalMarks"));

        // Add the constructed columns to the TableView
        tableViewTests.getColumns().addAll(idCol, testTitleCol, totalMarksCol);

        // Hook up the observable list with the TableView
        tableViewTests.setItems(testsObservableList);
    }

    @FXML
    public void onAddNewTestClick(ActionEvent event) {
        openTestDetails(TestDetailsPurpose.Add);
    }

    @FXML
    public void onEditSelectedTestClick(ActionEvent event) {
        openTestDetails(TestDetailsPurpose.Edit);
    }

    @FXML
    public void onRemoveSelectedTestClick(ActionEvent event) {
        // If a test is not selected then the action cannot proceed
        if (tableViewTests.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }

        // If the test has a result dependency, do not allow deletion
        ObservableList<Result> resultBank = FXCollections.observableArrayList();
        resultBank.addAll(DataPersistence.loadBank("resultBank"));

        Test selectedTest = ((Test) tableViewTests.getSelectionModel().getSelectedItem());

        for (Result result: resultBank) {   // For every result in the resultBank, check if it is based off the test that the user wishes to delete
            if (result.getTestUUID().toString().equals(selectedTest.getTestUUID().toString())) {
                new Alert(Alert.AlertType.ERROR, "This test has results from it, therefore it cannot be deleted. Delete the related results first to delete this.").show();
                return;
            }
        }

        // Now that we know a test is selected & it has no result dependencies, it can be deleted safely
        testsObservableList.remove(
                tableViewTests.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the questionsObservableList
        new Alert(Alert.AlertType.INFORMATION, "Test Deleted").show();
    }


    public void openTestDetails(TestDetailsPurpose testDetailsPurpose) {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewTests.getSelectionModel().getSelectedItem() == null && testDetailsPurpose != TestDetailsPurpose.Add) {
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/TestManagement/TestDetails.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Failed to load the TestDetails page").show();
        }

        Scene scene = new Scene(parent, 1200, 700);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        TestDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(testsObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (testDetailsPurpose) {
            case Add -> {
                stage.setTitle("Add New Test");
                dialogController.setCurrentUser(currentUser);
                dialogController.setTestDetailsPurpose(TestDetailsPurpose.Add);
                Test newTest = new Test(null,"", new ArrayList<>());
                testsObservableList.add(newTest);
                dialogController.setSelectedTest(newTest);

                // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
                stage.showAndWait();

                // If the user didn't finish adding the test, remove it
                if (newTest.getTestTitle().equals("")) {
                    testsObservableList.remove(newTest);
                }
            }
            case Edit -> {
                stage.setTitle("Edit Selected Test");
                dialogController.setCurrentUser(currentUser);
                dialogController.setTestDetailsPurpose(TestDetailsPurpose.Edit);
                dialogController.setSelectedTest((Test) tableViewTests.getSelectionModel().getSelectedItem());

                // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
                stage.showAndWait();
            }
            default -> throw new IllegalArgumentException();
        }

        tableViewTests.refresh();   // Updates the TableView so it can show the latest version of all tests
        // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.
        // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
        // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
    }

    public void setCurrentUser(User _currentUser) {
        currentUser = _currentUser;
    }

    @FXML
    public void onLoadTestsClick() {
        testsObservableList.clear();
        testsObservableList.addAll(DataPersistence.loadBank("testBank"));
    }

    @FXML
    public void onSaveTestsClick() {
        DataPersistence.saveBank("testBank", testsObservableList.stream().toList());
    }
}