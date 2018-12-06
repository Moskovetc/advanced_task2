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
import java.util.concurrent.atomic.AtomicInteger;

public class ManageAccounts {
    private IDataAccessObject dao = new DAO();
    private final int MAX_ACCOUNTS = 10;
    private final int MAX_THREADS = 20;
    public final static int MAX_TRANSACTIONS = 1000;
    private static AtomicInteger quantityTransactions = new AtomicInteger(1);
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
        ExecutorService service = Executors.newFixedThreadPool(MAX_THREADS);
        while (MAX_TRANSACTIONS > quantityTransactions.get()) {
            service.submit(new MoneyTransfer(dao.getAccounts(), quantityTransactions));
        }
        service.shutdown();
    }
}
