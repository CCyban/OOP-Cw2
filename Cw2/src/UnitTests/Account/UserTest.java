package UnitTests.Account;

import Classes.Account.SysAdmin;
import Classes.Account.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    private String firstName = "Bob";
    private String lastName = "Smith";
    private String username = "someUsername61";
    private String password = "aP@ssword123";

    @BeforeEach
    void setUp() {
        user = new SysAdmin(firstName, lastName, username, password);   // Cannot instantiate the user, but can use a subclass to test methods from the abstract class
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void getUserUUID() {
        UUID.fromString(user.getUserUUID().toString()); // Throws IllegalArgumentException if the user doesn't have a valid UUID
    }

    @Test
    void getFirstName() {
        assertEquals(firstName, user.getFirstName());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getLastName() {
        assertEquals(lastName, user.getLastName());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getFullName() {
        assertEquals(firstName + " " + lastName, user.getFullName());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getUsername() {
        assertEquals(username, user.getUsername());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getPassword() {
        assertEquals(password, user.getPassword());   // Checking if it matches the values given into the constructor
    }

    @Test
    void setFirstName() {
        String newFirstName = "James";
        user.setFirstName(newFirstName);
        assertEquals(newFirstName, user.getFirstName());   // Checking if the setter worked
    }

    @Test
    void setLastName() {
        String newLastName = "Johnson";
        user.setLastName(newLastName);
        assertEquals(newLastName, user.getLastName());   // Checking if the setter worked
    }

    @Test
    void setUsername() {
        String newUsername = "anotherUsername513";
        user.setUsername(newUsername);
        assertEquals(newUsername, user.getUsername());   // Checking if the setter worked
    }

    @Test
    void setPassword() {
        String newPassword = "s3cr3tP@ssword622";
        user.setPassword(newPassword);
        assertEquals(newPassword, user.getPassword());   // Checking if the setter worked
    }
}