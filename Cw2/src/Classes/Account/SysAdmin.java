package Classes.Account;

import Enums.AccountType;

import java.util.UUID;

public class SysAdmin extends User {

    public SysAdmin(String _firstName, String _lastName) {
        super(UUID.randomUUID(), _firstName, _lastName);
    }

    public AccountType getAccountType() {
        return AccountType.SysAdmin;
    }
}
