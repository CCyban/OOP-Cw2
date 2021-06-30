package Controllers.Tabs.QuestionManagement;

import Classes.DataPersistence;
import Classes.RegexTextFormatters;
import Classes.Quiz.Question;
import Enums.QuestionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Enums.QuestionType.*;

public class QuestionDetailsController implements Initializable {

    @FXML
    private Button buttonFinishQuestion;

    @FXML
    private ComboBox comboBoxQuestionTypeInput;

    @FXML
    private TextArea textAreaQuestionInput;

    @FXML
    private Label labelQuestionHelpText;

    @FXML
    private TextArea textAreaAnswerInput;

    @FXML
    private Label labelAnswerHelpText;

    @FXML
    private TextField textFieldAmountMarksInput;

    @FXML
    private TextField textFieldTagsInput;

    private ObservableList<Question> questionsObservableList;
    private QuestionManagementController.QuestionDetailsPurpose questionDetailsPurpose;
    private Question selectedQuestion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBoxItemsForInputs();
        initTextFormattersForInputs();
    }

    @FXML
    public void onFinishQuestionClick(ActionEvent event) {


        // The Gathering begins... (with some general validation)

        // Gather QuestionType value
        QuestionType questionTypeInput = (QuestionType) comboBoxQuestionTypeInput.getValue();
        if (questionTypeInput == null) {
            showIncompleteFormError();
            return;
        }

        // Gather Question value
        String questionInput = textAreaQuestionInput.getText();
        if (questionInput.isEmpty()) {
            showIncompleteFormError();
            return;
        }
        // Gather Answer value
        String answerInput = textAreaAnswerInput.getText();
        if (!textAreaAnswerInput.isDisable() && answerInput.isEmpty()) {
            showIncompleteFormError(); // Returns incomplete form alert if the user can input one & it is still empty
            return;
        }

        // Gather Amount of Marks value
        if (textFieldAmountMarksInput.getLength() == 0) {
            showIncompleteFormError();
            return;
        }

        // Cannot throw any exception since the TextFormatter makes it numbers-only
        int amountMarksInput = Integer.parseInt(textFieldAmountMarksInput.getText());

        // Gather a list of Tags
        List<String> tagsInput = Arrays.stream(textFieldTagsInput.getText().split(","))  // Makes a list with each new element after a comma, then converts it into a stream
                .map(String::strip)                                                            // Strips whitespace from the stream (means that it only strips the edges of the previous elements)
                .collect(Collectors.toList());                                                // Converts the stripped stream back into a list

        // The Gathering is complete


        // Question-type specific validation, am using switch statement here so new cases can easily be added whilst maintaining efficiency
        switch (questionTypeInput) {
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

        // Generate the question in text-form in preparation to show to the user for confirmation
        String contentText = getContentText(amountMarksInput, questionTypeInput, questionInput, answerInput, tagsInput);

        // Do the assigned action with confirmation dialogs
        doActionWithConfirmation(amountMarksInput, questionTypeInput, questionInput, answerInput, tagsInput, contentText);

        // Closes this dialog now that the action has been finished with
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setLocalObservableList(ObservableList<Question> _questionsObservableList) {
        this.questionsObservableList = _questionsObservableList;
    }

    public void setQuestionDetailsPurpose(QuestionManagementController.QuestionDetailsPurpose _questionDetailsPurpose) {
        this.questionDetailsPurpose = _questionDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (questionDetailsPurpose) {
            case Add:
                buttonFinishQuestion.setText("Create Question");
                // Must make the user select a question type first
                textAreaQuestionInput.setDisable(true);
                textAreaAnswerInput.setDisable(true);
                break;
            case Edit:
                buttonFinishQuestion.setText("Update Question");
                break;
            case Clone:
                buttonFinishQuestion.setText("Create Cloned Question");
                comboBoxQuestionTypeInput.setDisable(true); // User is not allowed to change type when cloning according to the requirements
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setSelectedQuestion(Question _selectedQuestion) {
        this.selectedQuestion = _selectedQuestion;

        comboBoxQuestionTypeInput.setValue(selectedQuestion.getQuestionType());
        textAreaQuestionInput.setText(selectedQuestion.getQuestion());
        textAreaAnswerInput.setText(selectedQuestion.getCorrectAnswer());
        textFieldAmountMarksInput.setText(String.valueOf(selectedQuestion.getCorrectMarks()));
        textFieldTagsInput.setText(selectedQuestion.getTags().toString().substring(1, selectedQuestion.getTags().toString().length() - 1)); // Using substring to trim the edges by 1 character each so that the []s are removed
    }

    public void showIncompleteFormError() {
        new Alert(Alert.AlertType.ERROR, "All required inputs are not filled in").show();
    }

    public void initComboBoxItemsForInputs() {
        // Inputting the questionTypes into the comboBox, any new types can be added here
        ObservableList<QuestionType> questionTypes = FXCollections.observableArrayList(
                QuestionType.values());
        comboBoxQuestionTypeInput.setItems(questionTypes);
    }

    public void initTextFormattersForInputs() {
        // Adding a integer-only TextFormatter to the textField 'Amount of Marks' input
        RegexTextFormatters.set3WholeNumbersOnlyTextFormatter(textFieldAmountMarksInput);

        // Adding a single-line only TextFormatter to the textArea 'Question' input
        RegexTextFormatters.setSingleLineTextFormatter(textAreaQuestionInput);

        // Applying the same single-line only TextFormatter to the textArea 'Answer' input
        RegexTextFormatters.setSingleLineTextFormatter(textAreaAnswerInput);
    }

    @FXML
    public void onQuestionTypeSelect(ActionEvent event) {
        // If one of the inputs is disabled then, enable them now that a question type is selected
        if (textAreaQuestionInput.isDisable()) {
            textAreaQuestionInput.setDisable(false);
            textAreaAnswerInput.setDisable(false);
        }

        // Always clear the text inputs when the user selects a question type so the applied TextFilter can work from the start
        textAreaQuestionInput.clear();
        textAreaAnswerInput.clear();

        updateUIBasedOnNewQuestionType((QuestionType) comboBoxQuestionTypeInput.getValue());
    }

    public void updateUIBasedOnNewQuestionType(QuestionType questionType) {
        switch (questionType) {
            case Arithmetic -> {
                // Set PromptText values
                labelQuestionHelpText.setText("Example: What is 2+2?");
                labelAnswerHelpText.setText("Example: 4\nOnly numbers and decimals can be used in the answer due to the question type");

                // Set TextFormatter
                RegexTextFormatters.setNumbersOnlyTextFormatter(textAreaAnswerInput);

                // Enable the answer box as this type uses it
                if (textAreaAnswerInput.isDisable()) {
                    textAreaAnswerInput.setDisable(false);
                }
            }
            case MultiChoice -> {
                // Set PromptText
                labelQuestionHelpText.setText("Example: What is 2+2?    (1, Two, 4, Orange)\nThe choices are a part of the question and are in a pair of () with each choice separated by ,");
                labelAnswerHelpText.setText("Example: 4");

                // Set TextFormatter to null as one isn't needed for this question type
                if (textAreaAnswerInput.getTextFormatter() != null) {
                    textAreaAnswerInput.setTextFormatter(null); // Overwrite the preexisting text-formatter
                }

                // Enable the answer box as this type uses it
                if (textAreaAnswerInput.isDisable()) {
                    textAreaAnswerInput.setDisable(false);
                }
            }
            case Manual -> {
                // Set PromptText
                labelQuestionHelpText.setText("Example: What is better: X or Y? Explain why");
                labelAnswerHelpText.setText("There are no pre-defined answers needed for manually marked questions such as this");

                // Set TextFormatter to null as one isn't needed for this question type
                if (textAreaAnswerInput.getTextFormatter() != null) {
                    textAreaAnswerInput.setTextFormatter(null); // Overwrite the preexisting text-formatter
                }

                // Disable the answer box as a manually marked question is directly marked by the teacher, not a pre-defined answer
                textAreaAnswerInput.setDisable(true);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public String getContentText(int amountMarksInput, QuestionType questionTypeInput, String questionInput, String answerInput, List<String> tagsInput) {
        String contentText;

        switch (questionTypeInput) {
            case Arithmetic -> contentText =
                    "Marks: " + amountMarksInput +
                            "\nQuestion Type: " + questionTypeInput +
                            "\nQuestion: " + questionInput +
                            "\nAnswer: " + answerInput +
                            "\nTags: " + tagsInput;
            case MultiChoice -> {   // Slightly more complicated since we have to extract a bit more information before showing the message (for this question type)
                // Storing a local copy of the question string as it will be modified
                String entireQuestion = questionInput;

                // Find the split of the subsections & remove the brackets to clean it up
                int optionsIndex = entireQuestion.indexOf('(');                     // Finds where initial question ends & the options begin
                entireQuestion = entireQuestion.replace("(", "");   // Removes the '('
                entireQuestion = entireQuestion.replace(")", "");   // Removes the ')'


                // Get subsections
                String initialQuestion = questionInput.substring(0, optionsIndex);  // Splits the entireQuestion into the initial question part
                String questionOptions = entireQuestion.substring(optionsIndex);    // Splits the entireQuestion into the question options part
                contentText =
                        "Marks: " + amountMarksInput +
                                "\nQuestion Type: " + questionTypeInput +
                                "\nQuestion: " + initialQuestion +
                                "\nChoices: " + Arrays.asList(questionOptions.split("\\s*,\\s*")) +
                                "\nAnswer: " + answerInput +
                                "\nTags: " + tagsInput;
            }
            case Manual -> contentText =
                    "Marks: " + amountMarksInput +
                            "\nQuestion Type: " + questionTypeInput +
                            "\nQuestion: " + questionInput +
                            "\nTags: " + tagsInput;
            default -> throw new IllegalArgumentException();
        }
        return contentText;
    }

    // 'Action' meaning the value in the questionDetailsPurpose enum
    public void doActionWithConfirmation(int amountMarksInput, QuestionType questionTypeInput, String questionInput, String answerInput, List<String> tagsInput, String contentText) {
        // Ask user if they are sure of their inputs
        Dialog<ButtonType> confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText("Are you sure?");
        confirmationDialog.setContentText(contentText);


        switch (questionDetailsPurpose) {
            case Add -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    // Load the gathered inputs into the constructor
                    Question newQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
                    // Add the new constructed question to the list
                    questionsObservableList.add(newQuestion);

                    new Alert(Alert.AlertType.CONFIRMATION, "The question is added to the question bank. Save the question bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("questionBank", questionsObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The question was not added.").show();
                }
            });
            case Edit -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    // Update the question data with the newly edited values
                    selectedQuestion.EditQuestion(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);

                    new Alert(Alert.AlertType.CONFIRMATION, "The question is edited. Save the question bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("questionBank", questionsObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The question was not edited.").show();
                }
            });
            case Clone -> confirmationDialog.showAndWait().ifPresent(confirmationResponse -> {
                if (confirmationResponse == ButtonType.OK) {    // If the user said they are OK with their inputs

                    // Create the newly made clone of the question
                    Question newClonedQuestion = new Question(questionInput, questionTypeInput, answerInput, amountMarksInput, tagsInput);
                    // Add the new constructed cloned question into the list
                    questionsObservableList.add(newClonedQuestion);

                    new Alert(Alert.AlertType.CONFIRMATION, "The question is cloned. Save the question bank now?").showAndWait().ifPresent(saveResponse -> {
                        if (saveResponse == ButtonType.OK) {
                            DataPersistence.saveBank("questionBank", questionsObservableList.stream().toList());
                        }
                    });
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "The question was not cloned.").show();
                }
            });
            default -> throw new IllegalArgumentException();
        }
    }
}