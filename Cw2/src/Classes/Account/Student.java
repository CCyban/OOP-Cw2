package Classes.Account;

import Enums.AccountType;

import java.util.Date;
import java.util.UUID;

public class Student extends User {

    private UUID classUUID;
    private Date dateOfBirth;

    public Student(String _firstName, String _lastName, Date _dateOfBirth) {
        super(UUID.randomUUID(), _firstName, _lastName);
        classUUID = UUID.randomUUID();
        dateOfBirth = _dateOfBirth;
    }

    public AccountType getAccountType() {
        return AccountType.Student;
    }

    public UUID getClassUUID() {
        return classUUID;
    }
}
