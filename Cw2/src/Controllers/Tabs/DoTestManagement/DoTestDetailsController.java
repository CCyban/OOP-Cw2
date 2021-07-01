package Controllers.Tabs.DoTestManagement;

import Classes.Account.User;
import Classes.DataPersistence;
import Classes.RegexTextFormatters;
import Classes.Quiz.Answer;
import Classes.Quiz.Question;
import Classes.Quiz.Result;
import Classes.Quiz.Test;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class DoTestDetailsController {

    private Test selectedTest;

    private Result selectedResult;

    private List<Question> testQuestions;

    @FXML
    private Button buttonFinishTest;

    @FXML
    private Label labelTestTitle;

    @FXML
    private Label labelTestMarksDescription;

    @FXML
    private Label labelTestMarksValue;

    @FXML
    private Accordion accordionTestQuestions;

    private ArrayList<Object> givenAnswerControlsArrayList = new ArrayList<>();

    private ArrayList<VBox> givenVBoxesArrayList = new ArrayList<>();

    private DoTestDetailsPurpose doTestDetailsPurpose;

    private User currentUser;

    public enum DoTestDetailsPurpose { Add, Edit, View };

    public void onFinishTestClick(ActionEvent event) {
        if (doTestDetailsPurpose.equals(DoTestDetailsPurpose.View)) {
            // Closes this dialog now that the purpose is complete
            ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
            return;
        }

        ArrayList<Answer> arrayListAnswers = new ArrayList<Answer>();

        for (int index = 0; index < givenAnswerControlsArrayList.size(); index++) {
            Question question = testQuestions.get(index);
            Object answer = givenAnswerControlsArrayList.get(index);
            int marksAchieved = 0;

            // Cannot use switch cases as they only work certain types
            if (answer.getClass() == ToggleGroup.class) {

                // If the user didn't select an answer, alert the user & abort the finish test procedure
                if (((ToggleGroup)answer).getSelectedToggle() == null) {
                    new Alert(Alert.AlertType.ERROR, "No option is selected in Question " + (index + 1)).show();
                    return;
                }

                // Extract the answer given from the selected radio button
                String answerGiven = ((RadioButton) ((ToggleGroup)answer).getSelectedToggle()).getText();

                // If the user got it correct, give the marks
                if (answerGiven.equals(question.getCorrectAnswer())) {
                    marksAchieved = question.getCorrectMarks();
                }

                // Save given answer with its details
                arrayListAnswers.add(new Answer(testQuestions.get(index).getQuestionUUID(), marksAchieved, answerGiven));
            }
            else if (answer.getClass() == TextField.class) {

                // Extract the answer given from the TextField input
                String answerGiven = ((TextField) answer).getText();

                // If the user didn't fill in an answer, alert the user & abort the finish test procedure
                if (answerGiven.isBlank()) {
                    new Alert(Alert.AlertType.ERROR, "Question " + (index + 1) + " is not filled in").show();
                    return;
                }

                // If the user got it correct, give the marks
                if (answerGiven.equals(question.getCorrectAnswer())) {
                    marksAchieved = question.getCorrectMarks();
                }

                // Save given answer with its details
                arrayListAnswers.add(new Answer(testQuestions.get(index).getQuestionUUID(), marksAchieved, answerGiven));
            }
            else if (answer.getClass() == TextArea.class) {

                // Extract the answer given from the TextArea input
                String answerGiven = ((TextArea) answer).getText();

                // If the user didn't fill in an answer, alert the user & abort the finish test procedure
                if (answerGiven.isBlank()) {
                    new Alert(Alert.AlertType.ERROR, "Question " + (index + 1) + " is not filled in").show();
                    return;
                }

                // Extracting the marks gained value
                marksAchieved = Integer.parseInt(((TextField)((HBox) givenVBoxesArrayList.get(index)
                        .getChildren().get(0))      // Getting the first element of the vbox, which is always a HBox
                        .getChildren().get(1))      // Getting the second element of the HBox, which is a TextField in this case
                        .getText());                // Extracting the latest value of the marks assigned by the teacher (as this is a manual question case)

                // Save given answer with its details
                arrayListAnswers.add(new Answer(testQuestions.get(index).getQuestionUUID(), marksAchieved, answerGiven));
            }
        }

        switch (doTestDetailsPurpose) {
            case Add -> DataPersistence.updateResultBank(true, new Result(selectedTest.getTestUUID(), currentUser.getUserUUID(), arrayListAnswers));
            case Edit -> {
                selectedResult.setResultData(arrayListAnswers);
                DataPersistence.updateResultBank(false, selectedResult);
            }
            default -> throw new IllegalArgumentException();
        }

        // Closes this dialog now that the purpose is complete
        ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
    }

    public void setSelectedTest(Test _selectedTest) {
        this.selectedTest = _selectedTest;

        this.testQuestions = selectedTest.getQuestions();

        labelTestTitle.setText(selectedTest.getTestTitle());

        // For each question in the test, we make a TitledPane. This code-generated TitlePane is then shown in the question list Accordion control.
        for (int index = 0; index < testQuestions.stream().count(); index++)
        {
            Question question = testQuestions.get(index);
            VBox vbox = new VBox();
            vbox.setSpacing(20);

            givenVBoxesArrayList.add(vbox);

            switch (question.getQuestionType()) {
                case Arithmetic -> {
                    // Adding the basic Correct Marks and Question labels to the question VBox
                    vbox.getChildren().add(
                            new HBox(
                                    new Label("Correct Marks: " + question.getCorrectMarks())));
                    vbox.getChildren().add(new Label("Question: " + question.getQuestion()));

                    // Done so we can keep track of the answers
                    TextField textFieldQuestion = new TextField();
                    givenAnswerControlsArrayList.add(textFieldQuestion);

                    // Setting a arithmetic-answers only TextFormatter to the answer TextField
                    RegexTextFormatters.setNumbersOnlyTextFormatter(textFieldQuestion);

                    // Creating and adding the answer HBox to the question VBox
                    HBox hbox = new HBox();
                    hbox.setSpacing(50);
                    hbox.getChildren().add(new Label("Answer: "));
                    hbox.getChildren().add(textFieldQuestion);
                    vbox.getChildren().add(hbox);
                }
                case MultiChoice -> {
                    // Storing a local copy of the question string as it will be modified
                    String entireQuestion = question.getQuestion();

                    // Find the split of the subsections & remove the brackets to clean it up
                    int optionsIndex = entireQuestion.indexOf('(');                     // Finds where initial question ends & the options begin
                    entireQuestion = entireQuestion.replace("(", "");   // Removes the '('
                    entireQuestion = entireQuestion.replace(")", "");   // Removes the ')'

                    // Get subsections
                    String initialQuestion = question.getQuestion().substring(0, optionsIndex); // Splits the entireQuestion into the initial question part
                    String optionsQuestion = entireQuestion.substring(optionsIndex);            // Splits the entireQuestion into the question options part

                    // Can now show a subsection - the initial question part
                    vbox.getChildren().add(
                            new HBox(
                                    new Label("Correct Marks: " + question.getCorrectMarks())));
                    Label labelQuestion = new Label("Question: " + initialQuestion);
                    labelQuestion.setWrapText(true);
                    vbox.getChildren().add(labelQuestion);

                    // Now to begin showing the answer options
                    vbox.getChildren().add(new Label("Answer: "));
                    ToggleGroup toggleGroupQuestion = new ToggleGroup();

                    // Done so we can keep track of the answers
                    givenAnswerControlsArrayList.add(toggleGroupQuestion);

                    // Now showing the other subsection - the options part
                    // For each option loop
                    for (String option: Arrays.asList(optionsQuestion.split("\\s*,\\s*"))) {
                        RadioButton radioButtonQuestion = new RadioButton(option);  // Creating a radio button for an answer option
                        radioButtonQuestion.setWrapText(true);
                        radioButtonQuestion.setToggleGroup(toggleGroupQuestion);    // Making the radio buttons functional by only letting 1 be selected
                        vbox.getChildren().add(radioButtonQuestion);                // Adding the radio button to the frontend
                    }
                }
                case Manual -> {
                    // Adding the basic Correct Marks and Question labels to the question VBox
                    vbox.getChildren().add(
                            new HBox(
                                new Label("Correct Marks: " + question.getCorrectMarks())));
                    vbox.getChildren().add(new Label("Question: " + question.getQuestion()));

                    // Done so we can keep track of the answers
                    TextArea textAreaQuestion = new TextArea();
                    textAreaQuestion.setPrefWidth(850);
                    givenAnswerControlsArrayList.add(textAreaQuestion);

                    // Creating and adding the answer HBox to the question VBox
                    HBox hbox = new HBox();
                    hbox.setSpacing(50);
                    hbox.getChildren().add(new Label("Answer: "));
                    hbox.getChildren().add(textAreaQuestion);
                    vbox.getChildren().add(hbox);
                }
                default -> throw new IllegalArgumentException();
            }

            TitledPane titledPane = new TitledPane("Question " + (index + 1), vbox);
            titledPane.setAnimated(true);

            accordionTestQuestions.getPanes().add(titledPane);
        }


    }

    public void setSelectedResult(Result _selectedResult) {
        this.selectedResult = _selectedResult;
        ArrayList givenAnswersArrayList = selectedResult.getResultData();

        for (int index = 0; index < givenAnswersArrayList.size(); index++) {

            Object indexAnswerControl = givenAnswerControlsArrayList.get(index);
            String indexAnswer = ((Answer)givenAnswersArrayList.get(index)).getGivenAnswer();

            // Cannot use switch cases as they only work certain types
            if (indexAnswerControl.getClass() == ToggleGroup.class) {

                // Set the result's chosen radio button to be selected
                ToggleGroup tg = ((ToggleGroup)indexAnswerControl);

                Toggle correctToggle = tg.getToggles().stream()
                        .filter(Toggle -> indexAnswer.equals(((RadioButton)Toggle).getText()))
                        .findFirst()
                        .orElse(null);
                correctToggle.setSelected(true);

                updateMarksLabelText(index);
            }
            else if (indexAnswerControl.getClass() == TextField.class) {

                // Set the givenAnswer to the TextField input
                ((TextField) indexAnswerControl).setText(indexAnswer);

                updateMarksLabelText(index);
            }
            else if (indexAnswerControl.getClass() == TextArea.class) {

                // Set the givenAnswer to the TextArea input
                ((TextArea) indexAnswerControl).setText(indexAnswer);

                // Getting the HBox which is marks-related, so we can tell the user how many marks they got based off their result
                HBox hbox = (HBox) givenVBoxesArrayList.get(index).getChildren().get(0);
                hbox.getChildren().clear();

                // Getting max marks for the indexed question
                int questionCorrectMarks = selectedTest.getQuestions().get(index).getCorrectMarks();

                Label labelMarksGained = new Label("Marks Gained: ");

                // Making TextField for marks gained, so the teacher can put the marks in (which only shows up on manual questions)
                TextField textFieldMarksGained = new TextField();
                textFieldMarksGained.setId("textFieldMarksGained");
                textFieldMarksGained.setPrefWidth(75);

                // Setting a marks-inputs only TextFormatter to the marks TextField
                RegexTextFormatters.set3WholeNumbersOnlyTextFormatter(textFieldMarksGained);

                textFieldMarksGained.setText(String.valueOf(selectedResult.getResultData().get(index).getMarksAchieved()));

                Label labelMaxMarks = new Label(" /" + questionCorrectMarks);

                hbox.getChildren().addAll(labelMarksGained, textFieldMarksGained, labelMaxMarks);
            }
        }
    }

    public void setTestDoPurpose(DoTestDetailsPurpose _doTestDetailsPurpose) {
        this.doTestDetailsPurpose = _doTestDetailsPurpose;

        // Alter some initial FXML details depending on the purpose of the visit
        switch (doTestDetailsPurpose) {
            case Add -> {
                buttonFinishTest.setText("Finish Test");
                labelTestMarksDescription.setText("Total Possible Marks:");
                labelTestMarksValue.setText(String.valueOf(selectedTest.getTotalMarks()));
            }
            case Edit -> {
                buttonFinishTest.setText("Save Any Modifications");
                labelTestMarksDescription.setText("Marks Gained:");
                labelTestMarksValue.setText(selectedResult.getTotalMarksAchieved() + "/" + selectedTest.getTotalMarks());
            }
            case View -> {
                buttonFinishTest.setText("Finish Viewing");
                labelTestMarksDescription.setText("Marks Gained:");
                labelTestMarksValue.setText(selectedResult.getTotalMarksAchieved() + "/" + selectedTest.getTotalMarks());

                int index = 0;
                for (Object obj: givenAnswerControlsArrayList) {
                    // Cannot use switch cases as they only work certain types
                    if (obj.getClass() == ToggleGroup.class) {
                        ((ToggleGroup)obj).getToggles().forEach(toggle -> {
                            Node node = (Node) toggle ;
                            node.setDisable(true);
                        });
                    }
                    else if (obj.getClass() == TextField.class) {
                        ((TextField)obj).setEditable(false);
                    }
                    else if (obj.getClass() == TextArea.class) {
                        ((TextArea)obj).setEditable(false);

                        ((HBox) givenVBoxesArrayList.get(index)
                                .getChildren().get(0))    // Getting the first element of the vbox, which is always a HBox
                                .getChildren().get(1)     // Getting the second element of the HBox, which is a TextField in this case
                                .setDisable(true);  // Disabling because the user is viewing the results, not editing
                    }
                    index++;
                }
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public void setCurrentUser(User _currentUser) {
        currentUser = _currentUser;
    }

    public void updateMarksLabelText(int index) {
        // Getting the HBox which is marks-related, so we can tell the user how many marks they got based off their result
        HBox hbox = (HBox) givenVBoxesArrayList.get(index).getChildren().get(0);

        // Getting max marks for the indexed question
        int questionCorrectMarks = selectedTest.getQuestions().get(index).getCorrectMarks();

        // Setting the new label text
        ((Label) hbox.getChildren().get(0)).setText("Marks Gained: " +
                selectedResult.getResultData().get(index).getMarksAchieved() +
                "/" +
                questionCorrectMarks);
    }
}