package Controllers.Tabs.DoTestManagement;

import Classes.Account.User;
import Classes.Class;
import Classes.DataPersistence;
import Classes.Quiz.Result;
import Classes.Quiz.Test;
import Enums.AccountType;
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

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class ViewTestResultsController implements Initializable {
    @FXML
    private TableView tableViewResults;

    @FXML
    private TextField textFieldSearch;

    private ObservableList<Result> resultsObservableList = FXCollections.observableArrayList();

    private ObservableList<Test> testsObservableList = FXCollections.observableArrayList();

    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Result> predicateContainsNonCaseStringOnly = q -> (q.getTestTitle().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewResults.setItems(resultsObservableList.filtered(predicateContainsNonCaseStringOnly));
        });

        /*
        // Load (if any) stored tests into a ObservableList
        resultsObservableList.clear();
        resultsObservableList.addAll(DataPersistence.loadBank("resultBank"));


         */

        // Load TableView with its columns & the newly made ObservableList
        initTableViewResults();
    }

    public void initTableViewResults() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Result Id");
        idCol.setPrefWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<Result, UUID>("resultUUID"));

        TableColumn userIdCol = new TableColumn("User Id"); // TODO: Swap out for user name or something more coherent
        userIdCol.setPrefWidth(100);
        userIdCol.setCellValueFactory(new PropertyValueFactory<Result, String>("userUUID"));

        TableColumn testTitleCol = new TableColumn("Test Title");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Result, String>("testTitle"));

        TableColumn marksGainedCol = new TableColumn("Marks Gained");
        marksGainedCol.setCellValueFactory(new PropertyValueFactory<Result, String>("totalMarksAchieved"));

        // Add the constructed columns to the TableView
        tableViewResults.getColumns().addAll(idCol, userIdCol, testTitleCol, marksGainedCol);

        // Hook up the observable list with the TableView
        tableViewResults.setItems(resultsObservableList);
    }


    public void onEditSelectedResultClick(ActionEvent event) {
        openTestView();
    }

    public void openTestView() {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewResults.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No test is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/DoTestManagement/DoTestDetails.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Failed to load the DoTestDetails dialog").show();
        }

        Scene scene = new Scene(parent, 1200, 700);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        DoTestDetailsController dialogController = fxmlLoader.getController();

        // Updating the stage & classes with key details depending on why the dialog is being used
        stage.setTitle("Test Attempt");

        Result result = (Result) tableViewResults.getSelectionModel().getSelectedItem();
        UUID testUUID = result.getTestUUID();

        testsObservableList.clear();
        testsObservableList.addAll(DataPersistence.loadBank("testBank"));

        Test selectedTest = testsObservableList.stream()
                .filter(test -> testUUID.equals(test.getTestUUID()))
                .findFirst()
                .orElse(null);

        dialogController.setSelectedTest(selectedTest);
        dialogController.setSelectedResult(result); // Important that this runs after the setSelectedTest as setSelectedTest generates the UI and this one fills it with the given answers
        if (currentUser.getAccountType() == AccountType.Student) {
            dialogController.setTestDoPurpose(DoTestDetailsController.DoTestDetailsPurpose.View);
        }
        else if (currentUser.getAccountType() == AccountType.Teacher) {
            dialogController.setTestDoPurpose(DoTestDetailsController.DoTestDetailsPurpose.Edit);
        }
        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();

        tableViewResults.refresh();     // Updates the TableView so it can show the latest version of an edited result
        // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.

        // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
        // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
    }

    @FXML
    public void onRemoveSelectedResultClick(ActionEvent event) {
        // If a result is not selected then the action cannot proceed
        if (tableViewResults.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No result is selected with your action").show();
            return;
        }

        resultsObservableList.remove(
                tableViewResults.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the resultsObservableList
        new Alert(Alert.AlertType.INFORMATION, "Result Deleted").show();
    }

    @FXML
    public void onLoadResultsClick(ActionEvent event) {
        loadOnlyMyResults();
    }

    @FXML
    public void onSaveResultsClick(ActionEvent event) {
        DataPersistence.saveBank("resultBank", resultsObservableList.stream().toList());
    }

    public void setCurrentUser(User _currentUser) {
        currentUser = _currentUser;

        loadOnlyMyResults();
    }

    public void loadOnlyMyResults() {
        if (currentUser.getAccountType() == AccountType.Student) {  // Goal is to load results from where the (signed in) student did the test
            resultsObservableList.clear();
            List<Result> resultBankList = DataPersistence.loadBank("resultBank");

            for (Result result: resultBankList) {
                if (currentUser.getUserUUID().equals(result.getUserUUID())) {
                    resultsObservableList.add(result);
                }
            }
        }
        else if (currentUser.getAccountType() == AccountType.Teacher) { // The goal is to load test results from classes that only the (signed in) teacher is involved in
            resultsObservableList.clear();
            List<Class> classBankList = DataPersistence.loadBank("classBank");
            List<Result> resultBankList = DataPersistence.loadBank("resultBank");

            ArrayList<Class> teacherClasses = new ArrayList<>();

            for (Class c: classBankList) {  // For each class in the classBank
                for (UUID uuid: c.getUserUUIDs()) { // For each UUID in a class
                    if (currentUser.getUserUUID().equals(uuid)) {   // If the teacher is a part of the class
                        teacherClasses.add(c);  // Add it to the teacherClasses array so we know
                    }
                }
            }

            for (Result result: resultBankList) {   // For each result in the resultBank
                UUID userUUID = result.getUserUUID();   // get the userUUID of the result

                for (Class tc: teacherClasses) {    // For each class the teacher is in
                    if (tc.getUserUUIDs().contains(userUUID)) { // If the result is a part of the class
                        resultsObservableList.add(result);  // Add result to the results list because the teacher should be able to see it
                    }
                }
            }

        }
    }
}
