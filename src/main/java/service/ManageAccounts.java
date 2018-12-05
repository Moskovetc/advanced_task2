package service;

import accounts.Account;
import dao.DAO;
import utilities.GenerateRandom;

import java.util.ArrayList;
import java.util.List;

public class ManageAccounts {
    private List<Account> accounts;
    DAO dao = new DAO();
    GenerateRandom generateRandom = new GenerateRandom();

    private void doTransactions() {
        for (int i = 0; i < 3; i++){
            Thread producerThread = new Thread(new MoneyTransfer(accounts));
            producerThread.start();
        }
    }

    private List<Account> createAccounts() {
        accounts = new ArrayList<Account>();
        for (int i = 0; i < 10; i++) {
            accounts.add(generateRandom.getAccount());
            dao.set(accounts.get(i));
        }
        return accounts;
    }

    private void deleteAccounts() {
        dao.deleteAll();
    }

    public void start() {
        deleteAccounts();
        createAccounts();
        doTransactions();
    }
}
