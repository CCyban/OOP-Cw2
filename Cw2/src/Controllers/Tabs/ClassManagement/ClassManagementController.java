package Controllers.Tabs.ClassManagement;

import Classes.Class;
import Classes.DataPersistence;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Predicate;

public class ClassManagementController implements Initializable {
    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewClasses;

    private ObservableList<Class> classesObservableList = FXCollections.observableArrayList();

    public enum ClassDetailsPurpose { Add, Edit };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if the subject of a class contains the string from the search (purposely not case-sensitive)
            Predicate<Class> predicateContainsNonCaseStringOnly = q -> (q.getSubject().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewClasses.setItems(classesObservableList.filtered(predicateContainsNonCaseStringOnly));

        });

        // Load all stored classes into an ObservableList
        classesObservableList.clear();
        classesObservableList.addAll(DataPersistence.loadBank("classBank"));

        // Load TableView with its columns & the newly made ObservableList
        initTableViewClasses();
    }

    public void initTableViewClasses() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Class Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Class, UUID>("classUUID"));
        idCol.setPrefWidth(100);

        TableColumn yearCol = new TableColumn("Year Group");
        yearCol.setCellValueFactory(new PropertyValueFactory<Class, String>("yearGroup"));

        TableColumn subjectCol = new TableColumn("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<Class, String>("Subject"));

        // Add the constructed columns to the TableView
        tableViewClasses.getColumns().addAll(idCol, yearCol, subjectCol);

        // Hook up the observable list with the TableView
        tableViewClasses.setItems(classesObservableList);
    }

    @FXML
    public void onAddNewClassClick(ActionEvent event) {
        openClassDetails(ClassDetailsPurpose.Add);
    }

    @FXML
    public void onEditSelectedClassClick(ActionEvent event) {
        openClassDetails(ClassDetailsPurpose.Edit);
    }

    @FXML
    public void onRemoveSelectedClassClick(ActionEvent event) {
        // If a user is not selected then the action cannot proceed
        if (tableViewClasses.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No class is selected with your action").show();
            return;
        }


        /*
        // If the question has a test dependency, do not allow deletion
        ObservableList<Test> testBank = FXCollections.observableArrayList();
        Banks.loadTestBank(false, true, testBank);

        Question selectedQuestion = ((Question) tableViewQuestions.getSelectionModel().getSelectedItem());

        for (Test test: testBank) { // For every test in the testBank

            List<UUID> testQuestionUUIDsList = test.getQuestionUUIDs();

            for (UUID questionUUID: testQuestionUUIDsList) {    // For every question in a test, check if it uses a question that the user wishes to delete
                if (questionUUID.toString().equals(selectedQuestion.getQuestionUUID().toString())) {
                    new Alert(Alert.AlertType.ERROR, "This user is used in results, therefore it cannot be deleted. Delete the related results first to delete this.").show();
                    return;
                }
            }
        }
        // TODO: Add results dependency
         */

        // Now that we know a user is selected & it has no result dependencies, it can be deleted safely
        classesObservableList.remove(
                tableViewClasses.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the usersObservableList
        new Alert(Alert.AlertType.INFORMATION, "Class Deleted").show();
    }

    public void openClassDetails(ClassDetailsPurpose classDetailsPurpose) {
        // If a user is not selected then the action cannot proceed (unless the user is adding an user, which doesn't require any selected user)
        if (tableViewClasses.getSelectionModel().getSelectedItem() == null && classDetailsPurpose != ClassDetailsPurpose.Add) {
            new Alert(Alert.AlertType.ERROR, "No class is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/ClassManagement/ClassDetails.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Failed to load the ClassDetails dialog").show();
        }

        Scene scene = new Scene(parent, 1200, 700);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        ClassDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(classesObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (classDetailsPurpose) {
            case Add -> {
                stage.setTitle("Add New Class");
                dialogController.setClassDetailsPurpose(ClassDetailsPurpose.Add);
                Class newClass = new Class("", "", new ArrayList<>());
                classesObservableList.add(newClass);
                dialogController.setSelectedClass(newClass);

                // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
                stage.showAndWait();

                // If the user didn't finish adding the class, remove it
                if (newClass.getYearGroup().equals("")) {
                    classesObservableList.remove(newClass);
                }
            }
            case Edit -> {
                stage.setTitle("Edit Selected Class");
                dialogController.setClassDetailsPurpose(classDetailsPurpose.Edit);
                dialogController.setSelectedClass((Class) tableViewClasses.getSelectionModel().getSelectedItem());

                // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
                stage.showAndWait();
            }
            default -> throw new IllegalArgumentException();
        }

        tableViewClasses.refresh();   // Updates the TableView so it can show the latest version of an edited class, or the values of a new class
        // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.

        // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
        // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
    }

    @FXML
    public void onLoadClassesClick() {
        classesObservableList.clear();
        classesObservableList.addAll(DataPersistence.loadBank("classBank"));
    }

    @FXML
    public void onSaveClassesClick() {
        DataPersistence.saveBank("classBank", classesObservableList.stream().toList());
    }
}
