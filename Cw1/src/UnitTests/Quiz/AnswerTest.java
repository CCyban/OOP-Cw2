package UnitTests.Quiz;

import Classes.Quiz.Answer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    private Answer answer = null;

    private UUID questionUUID = UUID.randomUUID();
    private int marksAchieved = 10;
    private String answerGiven = "42";

    @BeforeEach
    void setUp() {
        answer = new Answer(questionUUID, marksAchieved, answerGiven);
    }

    @AfterEach
    void tearDown() {
        answer = null;
    }

    @Test
    void getGivenAnswer() {
        assertEquals(answerGiven, answer.getGivenAnswer()); // Checking if it matches the values given into the constructor
    }
}