package Controllers.Tabs.DoTestManagement;

import Classes.Banks;
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
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class DoTestManagementController implements Initializable {
    @FXML
    private TableView tableViewTests;

    @FXML
    private TextField textFieldSearch;

    private ObservableList<Test> testsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Test> predicateContainsNonCaseStringOnly = q -> (q.getTestTitle().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewTests.setItems(testsObservableList.filtered(predicateContainsNonCaseStringOnly));
        });

        // Load (if any) stored questions into a ObservableList
        Banks.loadTestBank(false, true, testsObservableList);

        // Load TableView with its columns & the newly made ObservableList
        initTableViewTests();
    }


    @FXML
    public void onStartSelectedTestClick(ActionEvent event) {
        openTestView();
    }

    public void initTableViewTests() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Test Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Test, UUID>("testUUID"));

        TableColumn testTitleCol = new TableColumn("Test Title");
        testTitleCol.setCellValueFactory(new PropertyValueFactory<Test, String>("testTitle"));

        TableColumn totalMarksCol = new TableColumn("Total Possible Marks");
        totalMarksCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("totalMarks"));

        // Add the constructed columns to the TableView
        tableViewTests.getColumns().addAll(idCol, testTitleCol, totalMarksCol);

        // Hook up the observable list with the TableView
        tableViewTests.setItems(testsObservableList);
    }

    public void openTestView() {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewTests.getSelectionModel().getSelectedItem() == null) {
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
        stage.setTitle("Complete Test");
        dialogController.setSelectedTest(((Test) tableViewTests.getSelectionModel().getSelectedItem()));
        dialogController.setTestDoPurpose(DoTestDetailsController.DoTestDetailsPurpose.Add);

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();


        tableViewTests.refresh();   // Updates the TableView so it can show the latest version of all tests
        // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.
        // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
        // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
    }


    @FXML
    public void onLoadLatestTestsClick() {
        Banks.loadTestBank(true, true, testsObservableList);
    }

}