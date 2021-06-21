package UnitTests.Quiz;

import Classes.Quiz.Answer;
import Classes.Quiz.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private Result result = null;

    private UUID testUUID = UUID.randomUUID();
    private UUID userUUID = UUID.randomUUID();

    private Answer Answer1 = new Answer(UUID.randomUUID(), 8, "Some answer");
    private Answer Answer2 = new Answer(UUID.randomUUID(), 11, "ANOTHER ANSWER");
    private Answer Answer3 = new Answer(UUID.randomUUID(), 57, "sma11.ans3r");
    private ArrayList<Answer> resultData =
            new ArrayList<Answer>(Arrays.asList(Answer1, Answer2, Answer3));


    @BeforeEach
    void setUp() {
        result = new Result(testUUID, userUUID, resultData);
    }

    @AfterEach
    void tearDown() {
        result = null;
    }

    @Test
    void getResultUUID() {
        UUID.fromString(result.getResultUUID().toString()); // Throws IllegalArgumentException if the result doesn't have a valid UUID
    }

    @Test
    void getTestUUID() {
        assertEquals(testUUID, result.getTestUUID());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getUserUUID() {
        assertEquals(userUUID, result.getUserUUID());   // Checking if the it matches the values given into the constructor
    }

    @Test
    void getResultData() {
        assertEquals(resultData, result.getResultData());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getTotalMarksAchieved() {
        int totalAchievedMarks = 8 + 11 + 57;
        assertEquals(totalAchievedMarks, result.getTotalMarksAchieved());   // Checking if the maths add up from the values given into the constructor
    }

    @Test
    void setResultData() {
        Answer NewAnswer1 = new Answer(UUID.randomUUID(), 8, "Some answer");
        Answer NewAnswer2 = new Answer(UUID.randomUUID(), 11, "ANOTHER ANSWER");
        Answer NewAnswer3 = new Answer(UUID.randomUUID(), 60, "sma11.ans3r");
        Answer NewAnswer4 = new Answer(UUID.randomUUID(), 1, "321j2g12");
        Answer NewAnswer5 = new Answer(UUID.randomUUID(), 4, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        Answer NewAnswer6 = new Answer(UUID.randomUUID(), 72, ".-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.");
        Answer NewAnswer7 = new Answer(UUID.randomUUID(), 273, "asdasdasdasd");
        Answer NewAnswer8 = new Answer(UUID.randomUUID(), 84, "Yes");
        Answer NewAnswer9 = new Answer(UUID.randomUUID(), 1, "small test");
        Answer NewAnswer10 = new Answer(UUID.randomUUID(), 0, "BIG TEST");
        Answer NewAnswer11 = new Answer(UUID.randomUUID(), 617, "qwertyuiopasdfghjklzxcvbnm");
        Answer NewAnswer12 = new Answer(UUID.randomUUID(), 326, "89-043[t[pwr'eg#[34'/2235,23523523");

        ArrayList<Answer> newResultData =
                new ArrayList<Answer>(
                        Arrays.asList(
                                NewAnswer1, NewAnswer2, NewAnswer3, NewAnswer4,
                                NewAnswer5, NewAnswer6, NewAnswer7, NewAnswer8,
                                NewAnswer9, NewAnswer10, NewAnswer11, NewAnswer12));

        result.setResultData(newResultData);

        // Checking if the resultData is updated
        assertEquals(newResultData, result.getResultData());


        // Checking if the new total marks achieved value is also updated (since it relies on the result data)
        int totalAchievedMarks = 8 + 11 + 60 + 1 + 4 + 72 + 273 + 84 + 1 + 0 + 617 + 326;
        assertEquals(totalAchievedMarks, result.getTotalMarksAchieved());
    }
}