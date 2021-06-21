package Classes.Quiz;

import Classes.DataPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.UUID;

public class Result implements java.io.Serializable {

    private final UUID resultUUID;
    private final UUID testUUID;
    private final UUID userUUID;

    private ArrayList<Answer> resultData;

    public Result(UUID _testUUID, UUID _userUUID, ArrayList<Answer> _resultData) {
        // Generate a UUID for the result
        resultUUID = UUID.randomUUID();

        // Use payload values
        testUUID = _testUUID;
        userUUID = _userUUID;
        resultData = _resultData;
    }

    public UUID getResultUUID() {
        return resultUUID;
    }

    public UUID getTestUUID() {
        return testUUID;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getTestTitle() {
        ObservableList<Test> testBankObservableList = FXCollections.observableArrayList();
        testBankObservableList.addAll(DataPersistence.loadBank("testBank"));

        String testTitle = (testBankObservableList.stream()
                .filter(test -> testUUID.equals((test).getTestUUID()))
                .findFirst()
                .orElse(null)).getTestTitle();

        return testTitle;
    }

    public ArrayList<Answer> getResultData() {
        return resultData;
    }

    public int getTotalMarksAchieved() {
        int totalMarksAchieved = 0;
        for (Answer answer: resultData) {
            totalMarksAchieved += answer.getMarksAchieved();
        }
        return totalMarksAchieved;
    }

    public void setResultData(ArrayList<Answer> _resultData) {
        resultData = _resultData;
    }
}
