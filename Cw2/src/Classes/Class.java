package Classes;

import Classes.Account.User;
import Classes.Quiz.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Class implements java.io.Serializable{

    private final UUID classUUID;

    private String yearGroup;
    private String Subject;
    private ArrayList<UUID> userUUIDs;

    public Class(String _yearGroup, String _Subject, ArrayList<UUID> _userUUIDs) {
        // Generate a UUID for the class
        this.classUUID = UUID.randomUUID();

        // Use payload values
        this.yearGroup = _yearGroup;
        this.Subject = _Subject;
        this.userUUIDs = _userUUIDs;
    }

    public UUID getClassUUID() {
        return classUUID;
    }

    public String getYearGroup() {
        return yearGroup;
    }

    public void setYearGroup(String _yearGroup) {
        this.yearGroup = _yearGroup;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String _subject) {
        Subject = _subject;
    }

    public ArrayList<UUID> getUserUUIDs() {
        return userUUIDs;
    }

    public List<User> getUsers() {
        ObservableList<User> classUsersObservableList = FXCollections.observableArrayList();

        ObservableList<User> userBankObservableList = FXCollections.observableArrayList();
        Banks.loadUserBank(false, true, userBankObservableList);

        for (UUID userUUID: userUUIDs) {

            User currentUser = userBankObservableList.stream()
                    .filter(user -> userUUID.equals((user).getUserUUID()))
                    .findFirst()
                    .orElse(null);

            classUsersObservableList.add(currentUser);
        }
        return classUsersObservableList;
    }

    public void addUser(UUID userUUID) {
        userUUIDs.add(userUUID);
    }

    public void removeUser(UUID userUUID) {
        userUUIDs.remove(userUUID);
    }
}
