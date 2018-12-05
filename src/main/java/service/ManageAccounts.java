package service;

import accounts.Account;
import dao.DAO;
import utilities.GenerateRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageAccounts {
    private DAO dao = new DAO();
    private final int MAX_ACCOUNTS = 10;
    private final int MAX_THREADS = 20;

    private void createAccounts() {
        List<Account> accounts;
        GenerateRandom generateRandom = new GenerateRandom();
        accounts = new ArrayList<Account>();
        for (int i = 0; i < MAX_ACCOUNTS; i++) {
            accounts.add(generateRandom.getAccount());
            dao.set(accounts.get(i));
        }
    }

    private void deleteAccounts() {
        dao.deleteAll();
    }

    public void start() {
        deleteAccounts();
        createAccounts();
        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i = 0; i < MAX_THREADS; i++) {
            service.submit(new MoneyTransfer(dao.getAccounts()));
        }
        service.shutdown();
    }
}
