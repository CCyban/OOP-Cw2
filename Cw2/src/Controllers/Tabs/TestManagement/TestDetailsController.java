package Controllers.Tabs.TestManagement;

import Classes.Banks;
import Classes.Quiz.Question;
import Classes.Quiz.Test;
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
import java.util.*;
import java.util.function.Predicate;

public class TestDetailsController implements Initializable {

    @FXML
    private Button buttonFinishTest;

    @FXML
    private TextField textFieldTestTitleInput;

    @FXML
    private TableView tableViewQuestionBank;

    @FXML
    private TableView tableViewTestQuestions;


    @FXML
    private TextField textFieldQuestionBankSearchInput;

    @FXML
    private TextField textFieldTestQuestionsSearchInput;

    @FXML
    private Label labelTotalTestMarks;

    private ObservableList<Test> testObservableList;
    private TestManagementController.TestDetailsPurpose testDetailsPurpose;
    private Test selectedTest;

    private ObservableList<Question> questionBankObservableList = FXCollections.observableArrayList();
    private ObservableList<Question> testQuestionsObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldQuestionBankSearchInput.textProperty().addListener((Observable, oldValue, newValue) -> { // TODO: Make it search for a given multiple tags, not just one

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Question> predicateContainsNonCaseStringOnly = q -> (q.getTags().toString().toUpperCase().contains(textFieldQuestionBankSearchInput.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewQuestionBank.setItems(questionBankObservableList.filtered(predicateContainsNonCaseStringOnly));

        });
        textFieldTestQuestionsSearchInput.textProperty().addListener((Observable, oldValue, newValue) -> { // TODO: Make it search for a given multiple tags, not just one

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Question> predicateContainsNonCaseStringOnly = q -> (q.getTags().toString().toUpperCase().contains(textFieldTestQuestionsSearchInput.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewTestQuestions.setItems(testQuestionsObservableList.filtered(predicateContainsNonCaseStringOnly));

        });



        // Loads (if any) stored questions into a ObservableList
        Banks.loadQuestionBank(false, true, questionBankObservableList);

        initQuestionBankTableView();
        initTestQuestionsTableView();
    }

    @FXML
    public void onFinishTestClick(ActionEvent event) {
        // The Gathering begins...

        // Gather Test Title value
        String testTitleInput = textFieldTestTitleInput.getText();
        if (testTitleInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // No need to gather the testQuestions list because I am just referencing it directly

        // The Gathering is complete

        // Do the assigned action with confirmation dialogs
        doActionWithConfirmation(testTitleInput);

        // Closes this dialog now that the test is fully added/edited
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Test> _testsObservableList) {
        this.testObservableList = _testsObservableList;
    }

    public void setTestDetailsPurpose(TestManagementController.TestDetailsPurpose _testDetailsPurpose) {
        this.testDetailsPurpose = _testDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (testDetailsPurpose) {
            case Add: buttonFinishTest.setText("Create Test"); break;
            case Edit: buttonFinishTest.setText("Update Test"); break;
            default: throw new IllegalArgumentException();
        }
    }

    public void setSelectedTest(Test _selectedTest) {
        this.selectedTest = _selectedTest;

        this.testQuestionsObservableList = FXCollections.observableArrayList(selectedTest.getQuestions());

        textFieldTestTitleInput.setText(selectedTest.getTestTitle());

        tableViewTestQuestions.setItems(testQuestionsObservableList);
        labelTotalTestMarks.setText(String.valueOf(selectedTest.getTotalMarks()));
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initTestQuestionsTableView() {
        // Set the TableColumns up for the TableView
        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<Question, UUID>("questionUUID"));
        idCol.setPrefWidth(100);

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<Question, Enums.QuestionType>("questionType"));

        TableColumn correctMarksCol = new TableColumn("Possible Marks");
        correctMarksCol.setCellValueFactory(new PropertyValueFactory<Question, Integer>("CorrectMarks"));

        TableColumn tagsCol = new TableColumn("Tags");
        tagsCol.setCellValueFactory(new PropertyValueFactory<Question, List<String>>("Tags"));

        TableColumn questionCol = new TableColumn("Question");
        questionCol.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));

        TableColumn correctAnswerCol = new TableColumn("Correct Answer");
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));

        // Add the constructed columns to the TableView
        tableViewTestQuestions.getColumns().addAll(idCol, typeCol, correctMarksCol, tagsCol, questionCol, correctAnswerCol);

        // Hook up observable list with the TableView
        tableViewTestQuestions.setItems(testQuestionsObservableList);
    }

    public void initQuestionBankTableView() {
        // Set the TableColumns up for the TableView
        TableColumn idColQuestionBank = new TableColumn("Id");
        idColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, UUID>("questionUUID"));
        idColQuestionBank.setPrefWidth(100);

        TableColumn typeColQuestionBank = new TableColumn("Type");
        typeColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, Enums.QuestionType>("questionType"));

        TableColumn correctMarksColQuestionBank = new TableColumn("Possible Marks");
        correctMarksColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, Integer>("CorrectMarks"));

        TableColumn tagsColQuestionBank = new TableColumn("Tags");
        tagsColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, List<String>>("Tags"));

        TableColumn questionColQuestionBank = new TableColumn("Question");
        questionColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, String>("Question"));

        TableColumn correctAnswerColQuestionBank = new TableColumn("Correct Answer");
        correctAnswerColQuestionBank.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));

        // Add the constructed columns to the TableView
        tableViewQuestionBank.getColumns().addAll(idColQuestionBank, typeColQuestionBank,
                correctMarksColQuestionBank, tagsColQuestionBank, questionColQuestionBank, correctAnswerColQuestionBank);

        // Hook up observable list with the TableView
        tableViewQuestionBank.setItems(questionBankObservableList);
    }

    @FXML
    public void onAddToTestClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewQuestionBank.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }

        // Adds the question to the test
        selectedTest.addQuestion(((Question) tableViewQuestionBank.getSelectionModel().getSelectedItem()).getQuestionUUID());
        // Refreshes data/table
        testQuestionsObservableList = FXCollections.observableArrayList(selectedTest.getQuestions());
        tableViewTestQuestions.setItems(FXCollections.observableArrayList(selectedTest.getQuestions()));

        // Clear search now that the list has changed
        textFieldTestQuestionsSearchInput.setText("");
        tableViewTestQuestions.getSelectionModel().clearSelection();

        // Update total marks label
        labelTotalTestMarks.setText(String.valueOf(selectedTest.getTotalMarks()));
    }

    @FXML
    public void onRemoveFromTestClick(ActionEvent event) {
        // If a question is not selected then the action cannot proceed
        if (tableViewTestQuestions.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "No question is selected with your action").show();
            return;
        }

        // Removes the question from the test
        selectedTest.removeQuestion(((Question) tableViewTestQuestions.getSelectionModel().getSelectedItem()).getQuestionUUID());
        // Refreshes data/table
        testQuestionsObservableList = FXCollections.observableArrayList(selectedTest.getQuestions());
        tableViewTestQuestions.setItems(FXCollections.observableArrayList(selectedTest.getQuestions()));

        // Clear search & selection now that the list has changed
        textFieldTestQuestionsSearchInput.setText("");
        tableViewTestQuestions.getSelectionModel().clearSelection();

        // Update total marks label
        labelTotalTestMarks.setText(String.valueOf(selectedTest.getTotalMarks()));
    }

    // 'Action' meaning the value in the testDetailsPurpose enum
    public void doActionWithConfirmation(String testTitleInput) {
        // Ask user if they are sure of their inputs
        Dialog<ButtonType> confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure?");


        switch (testDetailsPurpose) {
            case Add -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    selectedTest.setTestTitle(testTitleInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The test is added to the test bank. Save the test bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            Banks.saveTestBank(true, true, testObservableList);
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The test was not added.").show();
                }
            });
            case Edit -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    selectedTest.setTestTitle(testTitleInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The test is edited. Save the test bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            Banks.saveTestBank(true, true, testObservableList);
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The test was not edited.").show();
                }
            });
            default -> throw new IllegalArgumentException();
        }
    }
}