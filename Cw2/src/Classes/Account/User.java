package Classes.Account;

import Enums.AccountType;
import java.util.Date;
import java.util.UUID;

public abstract class User implements java.io.Serializable {

    private UUID userUUID;

    private String firstName;
    private String lastName;

    private String username;
    private String password;

    public User(UUID _userUUID, String _firstName, String _lastName, String _username, String _password) {
        userUUID = _userUUID;
        firstName = _firstName;
        lastName = _lastName;
        username = _username;
        password = _password;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String _firstName) {
        this.firstName = _firstName;
    }

    public void setLastName(String _lastName) {
        this.lastName = _lastName;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public abstract AccountType getAccountType();

}
