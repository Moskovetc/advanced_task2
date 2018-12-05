package dao;

import accounts.Account;

public interface IDataAccessObject {
    Account get(String accountName);

    void set(Account account);

    void deleteAll();
}
