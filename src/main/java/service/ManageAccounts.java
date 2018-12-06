package service;

import accounts.Account;
import dao.DAO;
import dao.IDataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.GenerateRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageAccounts {
    private IDataAccessObject dao = new DAO();
    private final int MAX_ACCOUNTS = 10;
    private final int MAX_THREADS = 20;
    private final Logger logger = LoggerFactory.getLogger(ManageAccounts.class);

    private void createAccounts() {
        List<Account> accounts;
        GenerateRandom generateRandom = new GenerateRandom();
        accounts = new ArrayList<Account>();
        for (int i = 0; i < MAX_ACCOUNTS; i++) {
            accounts.add(generateRandom.getAccount());
            dao.set(accounts.get(i));
        }
        logger.debug(String.format("Started method createAccounts, Accounts created: %s", accounts));
    }

    private void deleteAccounts() {
        dao.deleteAll();
        logger.info("Started method deleteAccounts, folder was cleared");
    }

    public void start() {
        deleteAccounts();
        createAccounts();
        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i = 1; i < MAX_THREADS; i++) {
            logger.info(String.format("Started method start, thread %s started", i));
            service.submit(new MoneyTransfer(dao.getAccounts()));
        }
        service.shutdown();
    }
}
