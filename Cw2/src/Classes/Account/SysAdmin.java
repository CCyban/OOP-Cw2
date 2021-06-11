package Classes.Account;

import Enums.AccountType;

import java.util.UUID;

public class SysAdmin extends User {

    public SysAdmin(String _firstName, String _lastName, String _username, String _password) {
        super(UUID.randomUUID(), _firstName, _lastName, _username, _password);
    }

    public AccountType getAccountType() {
        return AccountType.SysAdmin;
    }
}
