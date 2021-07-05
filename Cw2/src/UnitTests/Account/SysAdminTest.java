package UnitTests.Account;

import Classes.Account.SysAdmin;
import Enums.AccountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysAdminTest {

    SysAdmin sysAdmin;

    private String firstName = "aFirstName";
    private String lastName = "someLastName";
    private String username = "randomUsername";
    private String password = "secretP@ssword67";

    @BeforeEach
    void setUp() {
        sysAdmin = new SysAdmin(firstName, lastName, username, password);
    }

    @AfterEach
    void tearDown() {
        sysAdmin = null;
    }

    @Test
    void getAccountType() {
        assertEquals(AccountType.SysAdmin, sysAdmin.getAccountType());   // Checking if the class matches the AccountType enum it is supposed to represent
    }
}