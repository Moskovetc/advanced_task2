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
    private AtomicInteger quantityTransactions;
    private final Long MAX_SUM = 10000000L;
    private GenerateRandom generateRandom = new GenerateRandom();
    private List<Account> accounts;
    private static final Logger logger = LoggerFactory.getLogger(MoneyTransfer.class);

    public MoneyTransfer(List<Account> accounts, AtomicInteger quantityTransactions) {
        this.accounts = accounts;
        this.quantityTransactions = quantityTransactions;
    }

    public void run() {
        Random random = new Random();
        Long sum = generateRandom.getSum(MAX_SUM);
        Account fromAccount = accounts.get(random.nextInt(accounts.size()));
        Account toAccount = accounts.get(random.nextInt(accounts.size()));
        if (checkAccountData(fromAccount, toAccount, sum)) {
            Transaction transaction = new Transaction(fromAccount, toAccount, sum, quantityTransactions);
            boolean transactionComplete = false;
            while (!transactionComplete) {
                transactionComplete = transaction.complete();
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
