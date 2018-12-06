package service;

import accounts.Account;
import dao.DAO;
import dao.IDataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.GenerateRandom;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManageAccounts {
    private IDataAccessObject dao = new DAO();
    private static final int MAX_ACCOUNTS = 10;
    private static final int MAX_THREADS = 20;
    private static final int MAX_TRANSACTIONS = 1000;
    private static final Logger logger = LoggerFactory.getLogger(ManageAccounts.class);

    private void createAccounts() {
        List<Account> accounts;
        GenerateRandom generateRandom = new GenerateRandom();
        accounts = new ArrayList<>();
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
        LocalDateTime transactionsStartAt;
        deleteAccounts();
        createAccounts();
        ExecutorService service = Executors.newFixedThreadPool(20);
                transactionsStartAt = LocalDateTime.now();
        for (int i = 1; i < MAX_THREADS; i++) {
            service.submit(new MoneyTransfer(dao.getAccounts()));
        }
        service.shutdown();
        logger.info(String.format("Transactions per second %s",
                getTransactionPerSecond(Duration.between(transactionsStartAt, LocalDateTime.now()))));
    }

    private float getTransactionPerSecond(Duration duration) {
        logger.info(String.format("Started method getTransactionPerSecond " +
                "with params: Duration %s (sec), MAX_Transactions: %s", duration.getSeconds(), MAX_TRANSACTIONS));
        return (float) duration.getSeconds() / (long) MAX_TRANSACTIONS;
    }

}
