package service;

import accounts.Account;
import exceptions.AccountsAreEqualsException;
import exceptions.NotEnoughBalanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.GenerateRandom;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MoneyTransfer implements Runnable {
    private static AtomicInteger quantityTransactions = new AtomicInteger(0);
    public static final int MAX_TRANSACTIONS = 1000;
    private static final Long MAX_SUM = 10000000L;
    private static final int MIN_TIME_SLEEP_TRANSACTION = 500;
    private static final int RANDOM_TIME_SLEEP_TRANSACTION = 500;
    private GenerateRandom generateRandom = new GenerateRandom();
    private List<Account> accounts;
    private static final Logger logger = LoggerFactory.getLogger(MoneyTransfer.class);

    public MoneyTransfer(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void run() {
        while (MAX_TRANSACTIONS > quantityTransactions.get()) {
            Random random = new Random();
            Long sum = generateRandom.getSum(MAX_SUM);
            Account fromAccount = accounts.get(random.nextInt(accounts.size()));
            Account toAccount = accounts.get(random.nextInt(accounts.size()));
            if (checkAccountData(fromAccount, toAccount, sum)) {
                Transaction transaction = new Transaction(fromAccount, toAccount, sum,
                        generateRandom.sleepTimer(MIN_TIME_SLEEP_TRANSACTION, RANDOM_TIME_SLEEP_TRANSACTION));
                quantityTransactions.incrementAndGet();
                if (!transaction.complete()){
                    quantityTransactions.decrementAndGet();
                    logger.error(String.format("Transaction was interrupted! %s", transaction));
                }
            }
        }
    }

    private boolean checkAccountData(Account fromAccount, Account toAccount, Long sum) {
        boolean result = false;
        if (null != fromAccount && null != toAccount) {
            if (fromAccount.getBalance() > sum) {
                if (fromAccount.getId().compareTo(toAccount.getId()) != 0) {
                    result = true;
                } else try {
                    throw new AccountsAreEqualsException();
                } catch (AccountsAreEqualsException e) {
                    logger.warn(String.format("%s Accounts from & to are equals. Account ID: %s",
                            Thread.currentThread().getName(), fromAccount.getId()));
                }
            } else try {
                throw new NotEnoughBalanceException();
            } catch (NotEnoughBalanceException e) {
                logger.warn(String.format("%s Account ID: %s Name: %s not enough sum trunsaction %s have sum %s",
                        Thread.currentThread().getName(), fromAccount.getId(), fromAccount.getAccountName(), sum,
                        fromAccount.getBalance()));
            }
        }
        logger.debug(String.format("Start method checkAccountData with params: fromAccount %s toAccount %s sum %s",
                fromAccount, toAccount, sum));
        return result;
    }
}
