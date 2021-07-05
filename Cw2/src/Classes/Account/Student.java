package Classes.Account;

import Enums.AccountType;

import java.time.LocalDate;
import java.util.UUID;

public class Student extends User {

    private LocalDate dateOfBirth;

    public Student(String _firstName, String _lastName, String _username, String _password, LocalDate _dateOfBirth) {
        super(UUID.randomUUID(), _firstName, _lastName, _username, _password);
        dateOfBirth = _dateOfBirth;
    }

    public AccountType getAccountType() {
        return AccountType.Student;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate _dateOfBirth) {
        this.dateOfBirth = _dateOfBirth;
    }
}
