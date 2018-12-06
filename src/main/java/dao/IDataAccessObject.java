package dao;

import accounts.Account;

import java.util.List;

public interface IDataAccessObject {
    Account get(String accountName);

    void set(Account account);

    List<String> getAccountNames();

    List<Account> getAccounts();

    void deleteAll();
}
