package Classes.Account;

import Enums.AccountType;
import java.util.Date;
import java.util.UUID;

abstract class User implements java.io.Serializable {

    private UUID userUUID;
    private String firstName;
    private String lastName;


    public User(UUID _userUUID, String _firstName, String _lastName) {
        userUUID = _userUUID;
        firstName = _firstName;
        lastName = _lastName;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public abstract AccountType getAccountType();

}
