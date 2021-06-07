package UnitTests.Quiz;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestTest {

    private Classes.Quiz.Test test = null;

    private String testTitle = "A unit test :)";

    private UUID question1UUID = UUID.randomUUID();
    private UUID question2UUID = UUID.randomUUID();
    private UUID question3UUID = UUID.randomUUID();
    private ArrayList<UUID> questionUUIDs =
            new ArrayList<UUID>(Arrays.asList(question1UUID, question2UUID, question3UUID));


    @BeforeEach
    void setUp() {
        test = new Classes.Quiz.Test(testTitle, questionUUIDs);
    }

    @AfterEach
    void tearDown() {
        test = null;
    }

    @Test
    void addQuestion() {
        int originalAmountOfQuestions = test.getQuestionUUIDs().size();
        UUID question4UUID = UUID.randomUUID();
        test.addQuestion(question4UUID);    // A new generated questionUUID to add to the test
        int newAmountOfQuestions = test.getQuestionUUIDs().size();

        // Checking if exactly 1 question is added
        assertEquals(originalAmountOfQuestions + 1, newAmountOfQuestions);

        // Checking if the question can be found in the test
        assertEquals(true, test.getQuestionUUIDs().contains(question4UUID));
    }

    @Test
    void removeQuestion() {
        int originalAmountOfQuestions = test.getQuestionUUIDs().size();
        test.removeQuestion(question2UUID); // This UUID already exists in the test from the setUp
        int newAmountOfQuestions = test.getQuestionUUIDs().size();

        // Checking if exactly 1 question is removed
        assertEquals(originalAmountOfQuestions - 1, newAmountOfQuestions);

        // Checking if the question is now nowhere to be found in the test
        assertEquals(false, test.getQuestionUUIDs().contains(question2UUID));
    }

    @Test
    void getQuestionUUIDs() {
        assertEquals(questionUUIDs, test.getQuestionUUIDs());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getTestUUID() {
        UUID.fromString(test.getTestUUID().toString()); // Throws IllegalArgumentException if the test doesn't have a valid UUID
    }

    @Test
    void getTestTitle() {
        assertEquals(testTitle, test.getTestTitle());   // Checking if it matches the values given into the constructor
    }

    @Test
    void setTestTitle() {
        String newTestTitle = "A new title!";
        test.setTestTitle(newTestTitle);
        assertEquals(newTestTitle, test.getTestTitle());    // Checking if the title is updated
    }
}