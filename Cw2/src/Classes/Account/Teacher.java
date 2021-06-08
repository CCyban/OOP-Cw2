package Classes.Account;

import Enums.AccountType;

import java.util.Date;
import java.util.UUID;

public class Teacher extends User {
    public Teacher(String _firstName, String _lastName) {
        super(UUID.randomUUID(), _firstName, _lastName);
    }

    public AccountType getAccountType() {
        return AccountType.Teacher;
    }
}
