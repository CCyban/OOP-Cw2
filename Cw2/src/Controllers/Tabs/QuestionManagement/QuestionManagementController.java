package Controllers.Tabs.QuestionManagement;

import Classes.DataPersistence;
import Classes.Quiz.Question;
import Classes.Quiz.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public class QuestionManagementController implements Initializable {

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView tableViewQuestions;

    private ObservableList<Question> questionsObservableList = FXCollections.observableArrayList();

    public enum QuestionDetailsPurpose { Add, Edit, Clone };


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldSearch.textProperty().addListener((Observable, oldValue, newValue) -> { // TODO: Make it search for a given multiple tags, not just one

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Question> predicateContainsNonCaseStringOnly = q -> (q.getTags().toString().toUpperCase().contains(textFieldSearch.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewQuestions.setItems(questionsObservableList.filtered(predicateContainsNonCaseStringOnly));

        });

        // Load (if any) stored questions into an ObservableList
        questionsObservableList.clear();
        questionsObservableList.addAll(DataPersistence.loadBank("questionBank"));

        // Load TableView with its columns & the newly made ObservableList
        initTableViewQuestions();
    }

    public void initTableViewQuestions() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Question Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Question, UUID>("questionUUID"));
        idCol.setPrefWidth(100);

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<Question, Enums.QuestionType>("questionType"));

        TableColumn correctMarksCol = new TableColumn("Possible Marks");
        correctMarksCol.setCellValueFactory(new PropertyValueFactory<Question, Integer>("correctMarks"));

        TableColumn tagsCol = new TableColumn("Tags");
        tagsCol.setCellValueFactory(new PropertyValueFactory<Question, List<String>>("Tags"));

        TableColumn questionCol = new TableColumn("Question");
        questionCol.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));

        TableColumn correctAnswerCol = new TableColumn("Correct Answer");
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));

        // Add the constructed columns to the TableView
        tableViewQuestions.getColumns().addAll(idCol, typeCol, correctMarksCol, tagsCol, questionCol, correctAnswerCol);

        // Hook up the observable list with the TableView
        tableViewQuestions.setItems(questionsObservableList);
    }

    @FXML
    public void onAddNewQuestionClick(ActionEvent event) {
        openQuestionDetails(QuestionDetailsPurpose.Add);
    }

    @FXML
    public void onEditSelectedQuestionClick(ActionEvent event) {
        openQuestionDetails(QuestionDetailsPurpose.Edit);
    }

    @FXML
    public void onCloneSelectedQuestionClick(ActionEvent event) {
        openQuestionDetails(QuestionDetailsPurpose.Clone);
    }

    @FXML
    public void onRemoveSelectedQuestionClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewQuestions.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }


        // If the question has a test dependency, do not allow deletion
        ObservableList<Test> testBank = FXCollections.observableArrayList();
        testBank.addAll(DataPersistence.loadBank("testBank"));

        Question selectedQuestion = ((Question) tableViewQuestions.getSelectionModel().getSelectedItem());

        for (Test test: testBank) { // For every test in the testBank

            List<UUID> testQuestionUUIDsList = test.getQuestionUUIDs();

            for (UUID questionUUID: testQuestionUUIDsList) {    // For every question in a test, check if it uses a question that the user wishes to delete
                if (questionUUID.toString().equals(selectedQuestion.getQuestionUUID().toString())) {
                    new Alert(Alert.AlertType.ERROR, "This question is used in tests, therefore it cannot be deleted. Delete the related tests first to delete this.").show();
                    return;
                }
            }
        }

        // Now that we know a question is selected & it has no test dependencies, it can be deleted safely
        questionsObservableList.remove(
                tableViewQuestions.getSelectionModel().getSelectedItem()
        ); // Removes the selected item from the questionsObservableList
        new Alert(Alert.AlertType.INFORMATION, "Question Deleted").show();
    }

    public void openQuestionDetails(QuestionDetailsPurpose questionDetailsPurpose) {
        // If a question is not selected then the action cannot proceed (unless the user is adding an question, which doesn't require any selected question)
        if (tableViewQuestions.getSelectionModel().getSelectedItem() == null && questionDetailsPurpose != QuestionDetailsPurpose.Add) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }

        // Stage configurations
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Tabs/QuestionManagement/QuestionDetails.fxml"));
        Parent parent = null;
        try {
             parent = fxmlLoader.load();
        }
        catch (IOException e)
        {
            new Alert(Alert.AlertType.ERROR, "Failed to load the QuestionDetails dialog").show();
        }

        Scene scene = new Scene(parent, 900, 500);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        QuestionDetailsController dialogController = fxmlLoader.getController();
        dialogController.setLocalObservableList(questionsObservableList);

        // Updating the stage & classes with key details depending on why the dialog is being used
        switch (questionDetailsPurpose) {
            case Add -> {
                stage.setTitle("Add New Question");
                dialogController.setQuestionDetailsPurpose(QuestionDetailsPurpose.Add);
            }
            case Edit -> {
                stage.setTitle("Edit Selected Question");
                dialogController.setQuestionDetailsPurpose(QuestionDetailsPurpose.Edit);
                dialogController.setSelectedQuestion((Question) tableViewQuestions.getSelectionModel().getSelectedItem());
            }
            case Clone -> {
                stage.setTitle("Clone Selected Question");
                dialogController.setQuestionDetailsPurpose(QuestionDetailsPurpose.Clone);
                dialogController.setSelectedQuestion((Question) tableViewQuestions.getSelectionModel().getSelectedItem());
            }
            default -> throw new IllegalArgumentException();
        }

        // The 'Wait' part in showAndWait means this method will wait here until the new stage is closed
        stage.showAndWait();

        if (questionDetailsPurpose == questionDetailsPurpose.Edit) {
            tableViewQuestions.refresh();   // Updates the TableView so it can show the latest version of an edited question
            // While ObservableList does observe the elements in the list, it doesn't seem to observe the values of one changing, giving cause for this to be used.

            // From the Java docs regarding the usage of the refresh method "This is useful in cases where the underlying data source has changed in a way that is not observed by the ListView itself"
            // Source - https://docs.oracle.com/javase/9/docs/api/javafx/scene/control/ListView.html
        }
    }

    @FXML
    public void onLoadQuestionsClick() {
        questionsObservableList.clear();
        questionsObservableList.addAll(DataPersistence.loadBank("questionBank"));
    }

    @FXML
    public void onSaveQuestionsClick() {
        DataPersistence.saveBank("questionBank", questionsObservableList.stream().toList());
    }
}