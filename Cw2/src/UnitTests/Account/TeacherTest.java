package UnitTests.Account;

import Classes.Account.Teacher;
import Enums.AccountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    Teacher teacher;

    private String firstName = "aTeacherFirstName";
    private String lastName = "someTeacherLastName";
    private String username = "funnyUsername";
    private String password = "topSecretPassword123";

    @BeforeEach
    void setUp() {
        teacher = new Teacher(firstName, lastName, username, password);
    }

    @AfterEach
    void tearDown() {
        teacher = null;
    }

    @Test
    void getAccountType() {
        assertEquals(AccountType.Teacher, teacher.getAccountType());   // Checking if the class matches the AccountType enum it is supposed to represent
    }
}