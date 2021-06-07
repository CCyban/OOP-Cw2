package UnitTests.Quiz;

import Classes.Quiz.Question;
import Enums.QuestionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    private Question question = null;

    private String questionString = "What is 6x6?";
    private QuestionType questionType = QuestionType.Arithmetic;
    private String correctAnswer = "36";
    private int correctMarks = 12;
    private List<String> Tags = Arrays.asList("Maths", "Year 2");

    @BeforeEach
    void setUp() {
        question = new Question(questionString, questionType, correctAnswer, correctMarks, Tags);
    }

    @AfterEach
    void tearDown() {
        question = null;
    }

    @Test
    void editQuestion() {
        UUID originalUUID = question.getQuestionUUID();

        String newQuestionString = "What is 3+3?";
        String newCorrectAnswer = "6";
        List<String> newTags = Arrays.asList("Maths", "Year 2", "KS1");

        // Edit some properties of the Question
        question.EditQuestion(newQuestionString, questionType, newCorrectAnswer, correctMarks, newTags);

        // Ensure the UUID didn't change while editing
        assertEquals(originalUUID, question.getQuestionUUID());

        // Ensure the edited values are actually edited
        assertEquals(newQuestionString, question.getQuestion());
        assertEquals(newCorrectAnswer, question.getCorrectAnswer());
        assertEquals(newTags, question.getTags());

        // Ensure the rest are the unchanged
        assertEquals(questionType, question.getQuestionType());
        assertEquals(correctMarks, question.getCorrectMarks());
    }

    @Test
    void getQuestionUUID() {
        UUID.fromString(question.getQuestionUUID().toString()); // Throws IllegalArgumentException if the question doesn't have a valid UUID
    }

    @Test
    void getQuestionType() {
        assertEquals(questionType, question.getQuestionType()); // Checking if it matches the values given into the constructor
    }

    @Test
    void getQuestion() {
        assertEquals(questionString, question.getQuestion());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getCorrectAnswer() {
        assertEquals(correctAnswer, question.getCorrectAnswer());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getCorrectMarks() {
        assertEquals(correctMarks, question.getCorrectMarks()); // Checking if it matches the values given into the constructor
    }

    @Test
    void getTags() {
        assertEquals(Tags, question.getTags()); // Checking if it matches the values given into the constructor
    }
}