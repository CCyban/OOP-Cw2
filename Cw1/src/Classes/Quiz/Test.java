package Classes.Quiz;

import Classes.Banks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class Test implements java.io.Serializable {

    private final UUID testUUID;
    private String testTitle;
    private ArrayList<UUID> questionUUIDs;

    public Test(String _testTitle, ArrayList<UUID> _questionUUIDs) {
        // Generate a UUID for the test
        testUUID = UUID.randomUUID();

        // Use payload values
        testTitle = _testTitle;
        questionUUIDs = _questionUUIDs;
    }

    public void addQuestion(UUID questionUUID) {
        questionUUIDs.add(questionUUID);
    }

    public void removeQuestion(UUID questionUUID) {
        questionUUIDs.remove(questionUUID);
    }

    public List<UUID> getQuestionUUIDs() { return questionUUIDs; }

    public UUID getTestUUID() {
        return testUUID;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String _testTitle) {
        this.testTitle = _testTitle;
    }

    public List<Question> getQuestions() {
            ObservableList<Question> testQuestionsObservableList = FXCollections.observableArrayList();

        ObservableList<Question> questionBankObservableList = FXCollections.observableArrayList();
        Banks.loadQuestionBank(false, true, questionBankObservableList);

        for (UUID questionUUID: questionUUIDs) {

            Question currentQuestion = questionBankObservableList.stream()
                    .filter(question -> questionUUID.equals((question).getQuestionUUID()))
                    .findFirst()
                    .orElse(null);

            testQuestionsObservableList.add(currentQuestion);
        }
        return testQuestionsObservableList;
    }

    public int getTotalMarks() {
        ObservableList<Question> questionBankObservableList = FXCollections.observableArrayList();
        Banks.loadQuestionBank(false, true, questionBankObservableList);

        int totalMarks = 0;

        for (UUID questionUUID: questionUUIDs) {

            Question currentQuestion = questionBankObservableList.stream()
                    .filter(question -> questionUUID.equals((question).getQuestionUUID()))
                    .findFirst()
                    .orElse(null);

            totalMarks += currentQuestion.getCorrectMarks();
        }
        return totalMarks;
    }
}
