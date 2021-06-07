package Classes.Quiz;

import Enums.QuestionType;
import java.util.List;
import java.util.UUID;

public class Question implements java.io.Serializable {

    private final UUID questionUUID;
    private QuestionType questionType;
    private String Question;
    private String correctAnswer;
    private List<String> Tags;
    private int correctMarks;

    public Question(String _Question, QuestionType _questionType, String _correctAnswer, int _correctMarks, List<String> _Tags)
    {
        // Generate a UUID for the question
        questionUUID = UUID.randomUUID();

        // Use payload values
        Question = _Question;
        questionType = _questionType;
        correctAnswer = _correctAnswer;
        correctMarks = _correctMarks;
        Tags = _Tags;
    }

    public void EditQuestion(String _Question, QuestionType _questionType, String _correctAnswer, int _correctMarks, List<String> _Tags) {
        if (!Question.equals(_Question)) {
            Question = _Question;
        }
        if (!questionType.equals(_questionType)) {
            questionType = _questionType;
        }
        if (!correctAnswer.equals(_correctAnswer)) {
            correctAnswer = _correctAnswer;
        }
        if (correctMarks != _correctMarks) {
            correctMarks = _correctMarks;
        }
        if (!Tags.equals(_Tags)) {
            Tags = _Tags;
        }
    }

    public UUID getQuestionUUID() {
        return questionUUID;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public String getQuestion() {
        return Question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getCorrectMarks() {
        return correctMarks;
    }

    public List<String> getTags() {
        return Tags;
    }
}