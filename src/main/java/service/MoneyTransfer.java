package service;

import accounts.Account;
import dao.DAO;
import exceptions.NotEnoughBalanceException;
import utilities.GenerateRandom;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MoneyTransfer implements Runnable {
    private ReentrantLock lock = new ReentrantLock();
    private static AtomicInteger quantityTransactions = new AtomicInteger(0);
    private final int MAX_TRANSACTIONS = 100;
    private GenerateRandom generateRandom = new GenerateRandom();
    private DAO dao = new DAO();
    private List<Account> accounts;

    public MoneyTransfer(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void run() {
        while (MAX_TRANSACTIONS > quantityTransactions.get()) {
//            Long fromAccountID = generateRandom.getId();
//            Long toAccountID = generateRandom.getId();
            Random random = new Random();
            Long fromAccountID = (long) random.nextInt(10);
            Long toAccountID = (long) random.nextInt(10);
            Long sum = generateRandom.getSum(1000);
            String fromAccountName = DAO.PATH_PREFIX + fromAccountID;
            String toAccountName = DAO.PATH_PREFIX + toAccountID;
            Account fromAccount = dao.get(fromAccountName);
            Account toAccount = dao.get(toAccountName);
            if (lock.tryLock()) {
                if (checkAccountData(fromAccount, toAccount, sum)) {
                    transaction(fromAccount, toAccount, sum);
                    quantityTransactions.incrementAndGet();
                }
            }
        }
    }

    private boolean checkAccountData(Account fromAccount, Account toAccount, Long sum) {
        if (null != fromAccount && null != toAccount) {
            if (fromAccount.getBalance() > sum) {
                return true;
            } else try {
                throw new NotEnoughBalanceException();
            } catch (NotEnoughBalanceException e) {
                System.err.println(String.format("Account %s not enough sum %s", fromAccount.getAccountName(), sum));
            }
        }
        return false;
    }

    private void transaction(Account fromAccount, Account toAccount, Long sum) {
        System.out.println(String.format("%s %s %s %s",  Thread.currentThread().getName(), fromAccount.getBalance(),
                toAccount.getBalance(), sum));
        fromAccount.setBalance(fromAccount.getBalance() - sum);
        toAccount.setBalance(toAccount.getBalance() + sum);
        System.out.println(String.format("%s %s %s %s",  Thread.currentThread().getName(), fromAccount.getBalance(),
                toAccount.getBalance(), sum));
    }
}