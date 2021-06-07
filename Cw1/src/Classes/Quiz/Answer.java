package Classes.Quiz;

import java.util.UUID;

public class Answer implements java.io.Serializable {

    private UUID questionUUID;
    private int marksAchieved;
    private String givenAnswer;

    public Answer(UUID _questionUUID, int _marksAchieved, String _givenAnswer) {
        questionUUID = _questionUUID;
        marksAchieved = _marksAchieved;
        givenAnswer = _givenAnswer;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    public int getMarksAchieved() {
        return marksAchieved;
    }
}
