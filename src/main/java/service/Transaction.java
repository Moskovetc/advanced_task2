package service;

import accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Transaction {
    private Account fromAccount;
    private Account toAccount;
    private Long sum;
    private AtomicInteger quantityTransactions;
    private static final int MIN_TIME_FOR_THREAD_SLEEP = 0;
    private static final int RANDOM_TIME_FOR_THREAD_SLEEP = 0;
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Account fromAccount, Account toAccount, Long sum, AtomicInteger quantityTransactions) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.sum = sum;
        this.quantityTransactions = quantityTransactions;
    }

    public boolean complete() {
        boolean notSucsessfull = false;

        if (fromAccount.getId() < toAccount.getId()) {
            if (fromAccount.getLock().tryLock()) {
                try {
                    if (toAccount.getLock().tryLock()) {
                        try {
//                            randomThreadSleep();
                            if (quantityTransactions.get() < ManageAccounts.MAX_TRANSACTIONS) {
                                transfer(fromAccount, toAccount, sum);
                                notSucsessfull = true;
                            } else {
                                notSucsessfull = true;
                            }
                            quantityTransactions.incrementAndGet();
                        } finally {
                            toAccount.getLock().unlock();
                        }
                    }
                } finally {
                    fromAccount.getLock().unlock();
                }
            }
        } else {
            if (toAccount.getLock().tryLock()) {
                try {
                    if (fromAccount.getLock().tryLock()) {
                        try {
//                            randomThreadSleep();
                            if (quantityTransactions.get() < ManageAccounts.MAX_TRANSACTIONS) {
                                transfer(fromAccount, toAccount, sum);
                                notSucsessfull = true;
                            } else {
                                notSucsessfull = true;
                            }
                            quantityTransactions.incrementAndGet();
                        } finally {
                            toAccount.getLock().unlock();
                        }
                    }
                } finally {
                    fromAccount.getLock().unlock();
                }
            }
        }

        logger.info(String.format("Transaction complete with params: fromAccount %s toAccount %s sum %s",
                fromAccount, toAccount, sum));
        return notSucsessfull;
    }

    private void transfer(Account fromAccount, Account toAccount, Long sum) {
        fromAccount.setBalance(fromAccount.getBalance() - sum);
        toAccount.setBalance(toAccount.getBalance() + sum);
    }

    private void randomThreadSleep() {
        Random random = new Random();
        try {
            Thread.sleep(MIN_TIME_FOR_THREAD_SLEEP + random.nextInt(RANDOM_TIME_FOR_THREAD_SLEEP));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
