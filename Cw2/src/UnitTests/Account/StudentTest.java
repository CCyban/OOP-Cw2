package UnitTests.Account;

import Classes.Account.Student;
import Enums.AccountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    Student student;

    private String firstName = "Alice";
    private String lastName = "Brown";
    private String username = "user7945";
    private String password = "£%g23defog";
    private LocalDate dateOfBirth = LocalDate.ofYearDay(2005, 72);

    @BeforeEach
    void setUp() {
        student = new Student(firstName, lastName, username, password, dateOfBirth);
    }

    @AfterEach
    void tearDown() {
        student = null;
    }

    @Test
    void getAccountType() {
        assertEquals(AccountType.Student, student.getAccountType());   // Checking if the class matches the AccountType enum it is supposed to represent
    }

    @Test
    void getDateOfBirth() {
        assertEquals(dateOfBirth, student.getDateOfBirth());    // Checking if it matches the values given into the constructor
    }

    @Test
    void setDateOfBirth() {
        LocalDate newDateOfBirth = LocalDate.ofYearDay(2012, 251);
        student.setDateOfBirth(newDateOfBirth);
        assertEquals(newDateOfBirth, student.getDateOfBirth());    // Checking if the setter worked
    }
}