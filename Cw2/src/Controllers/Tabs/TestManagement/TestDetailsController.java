package Controllers.Tabs.TestManagement;

import Classes.Account.User;
import Classes.Class;
import Classes.DataPersistence;
import Classes.Quiz.Question;
import Classes.Quiz.Test;
import Enums.QuestionType;
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

import static Enums.QuestionType.Arithmetic;
import static Enums.QuestionType.MultiChoice;

public class TestDetailsController implements Initializable {

    @FXML
    private Button buttonFinishTest;

    @FXML
    private TextField textFieldTestTitleInput;

    @FXML
    private ComboBox comboBoxAssignedClassInput;

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

    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Listeners
        textFieldQuestionBankSearchInput.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Question> predicateContainsNonCaseStringOnly = q -> (q.getTags().toString().toUpperCase().contains(textFieldQuestionBankSearchInput.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewQuestionBank.setItems(questionBankObservableList.filtered(predicateContainsNonCaseStringOnly));

        });
        textFieldTestQuestionsSearchInput.textProperty().addListener((Observable, oldValue, newValue) -> {

            // Simply returns true if a tag from a question contains the string from the search (purposely not case-sensitive)
            Predicate<Question> predicateContainsNonCaseStringOnly = q -> (q.getTags().toString().toUpperCase().contains(textFieldTestQuestionsSearchInput.getText().toUpperCase()));

            // TableView now gets the latest version of the filtered ObservableList
            tableViewTestQuestions.setItems(testQuestionsObservableList.filtered(predicateContainsNonCaseStringOnly));

        });



        // Loads (if any) stored questions into a ObservableList
        questionBankObservableList.clear();
        questionBankObservableList.addAll(DataPersistence.loadBank("questionBank"));

        initQuestionBankTableView();
        initTestQuestionsTableView();
    }

    @FXML
    public void onFinishTestClick(ActionEvent event) {
        // The Gathering begins...

        // Gather assigned class value
        UUID assignedClassUUIDInput = null;
        if (comboBoxAssignedClassInput.getValue() == null) {
            showIncompleteFormError();
            return;
        }
        String assignedClassYearGroupInput = comboBoxAssignedClassInput.getValue().toString();
        List<Class> classBank = DataPersistence.loadBank("classBank");
        for (Class c: classBank) {
            if (c.getYearGroup().equals(assignedClassYearGroupInput)) {
                assignedClassUUIDInput = c.getClassUUID();
            }
        }

        // Gather Test Title value
        String testTitleInput = textFieldTestTitleInput.getText();
        if (testTitleInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }

        // The Gathering is complete

        // Do the assigned action with confirmation dialogs
        doActionWithConfirmation(testTitleInput, assignedClassUUIDInput);

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

        List<Class> classBank = DataPersistence.loadBank("classBank");

        if (selectedTest.getClassUUID() != null) {
            for (Class c : classBank) {
                if (selectedTest.getClassUUID().equals(c.getClassUUID())) {
                    comboBoxAssignedClassInput.setValue(c.getYearGroup());
                    break;
                }
            }
        }
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

    public void initComboBoxItemsForInputs() {
        // Inputting the classes into the comboBox
        ObservableList<String> userClassesObservableList = FXCollections.observableArrayList();

        ObservableList<Class> classBankObservableList = FXCollections.observableArrayList();
        classBankObservableList.addAll(DataPersistence.loadBank("classBank"));

        for (Class c: classBankObservableList) {
            for (UUID uuid: c.getUserUUIDs()) {
                if (currentUser.getUserUUID().equals(uuid)) {
                    userClassesObservableList.add(c.getYearGroup());

                }
            }
        }
        comboBoxAssignedClassInput.setItems(userClassesObservableList);
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
    public void doActionWithConfirmation(String testTitleInput, UUID assignedClassUUIDInput) {
        // Ask user if they are sure of their inputs
        Dialog<ButtonType> confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure?");


        switch (testDetailsPurpose) {
            case Add -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    selectedTest.setTestTitle(testTitleInput);
                    selectedTest.setClassUUID(assignedClassUUIDInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The test is added to the test bank. Save the test bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("testBank", testObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The test was not added.").show();
                }
            });
            case Edit -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    selectedTest.setTestTitle(testTitleInput);
                    selectedTest.setClassUUID(assignedClassUUIDInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The test is edited. Save the test bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("testBank", testObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The test was not edited.").show();
                }
            });
            default -> throw new IllegalArgumentException();
        }
    }

    public void setCurrentUser(User _currentUser) {
        currentUser = _currentUser;
        initComboBoxItemsForInputs();
    }
}