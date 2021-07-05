package UnitTests.Quiz;

import Classes.Quiz.Class;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest {

    private Class aClass;

    private String yearGroup = "Year 2";
    private String Subject = "Maths";

    private UUID user1UUID = UUID.randomUUID();
    private UUID user2UUID = UUID.randomUUID();
    private UUID user3UUID = UUID.randomUUID();
    private ArrayList<UUID> userUUIDs =
            new ArrayList<UUID>(Arrays.asList(user1UUID, user2UUID, user3UUID));


    @BeforeEach
    void setUp() {
        aClass = new Class(yearGroup, Subject, userUUIDs);
    }

    @AfterEach
    void tearDown() {
        aClass = null;
    }

    @Test
    void getClassUUID() {
        UUID.fromString(aClass.getClassUUID().toString()); // Throws IllegalArgumentException if the class doesn't have a valid UUID
    }

    @Test
    void getYearGroup() {
        assertEquals(yearGroup, aClass.getYearGroup());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getSubject() {
        assertEquals(Subject, aClass.getSubject());   // Checking if it matches the values given into the constructor
    }

    @Test
    void getUserUUIDs() {
        assertEquals(userUUIDs, aClass.getUserUUIDs());   // Checking if it matches the values given into the constructor
    }

    @Test
    void addUser() {
        int originalAmountOfUsers = aClass.getUserUUIDs().size();
        UUID user4UUID = UUID.randomUUID();
        aClass.addUser(user4UUID);    // A new generated userUUID to add to the class
        int newAmountOfUsers = aClass.getUserUUIDs().size();

        // Checking if exactly 1 user is added
        assertEquals(originalAmountOfUsers + 1, newAmountOfUsers);

        // Checking if the user can be found in the class
        assertEquals(true, aClass.getUserUUIDs().contains(user4UUID));
    }

    @Test
    void removeUser() {
        int originalAmountOfUsers = aClass.getUserUUIDs().size();
        aClass.removeUser(user2UUID); // This UUID already exists in the test from the setUp
        int newAmountOfUsers = aClass.getUserUUIDs().size();

        // Checking if exactly 1 question is removed
        assertEquals(originalAmountOfUsers - 1, newAmountOfUsers);

        // Checking if the question is now nowhere to be found in the test
        assertEquals(false, aClass.getUserUUIDs().contains(user2UUID));
    }
}