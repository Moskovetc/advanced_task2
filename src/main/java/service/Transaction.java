package service;

import accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Transaction {
    private Account fromAccount;
    private Account toAccount;
    private Long sum;
    private static AtomicInteger quantityTransactions;
    private static final int MAX_TRANSACTIONS = 1000;
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Account fromAccount, Account toAccount, Long sum, AtomicInteger quantityTransactions) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.sum = sum;
        this.quantityTransactions = quantityTransactions;
    }

    public void complete() {
        boolean notSucsessfull = true;
        while (notSucsessfull) {
            if (fromAccount.getId() < toAccount.getId()) {
                if (fromAccount.getLock().tryLock()) {
                    try {
                        if (toAccount.getLock().tryLock()) {
                            try {
                                if (quantityTransactions.get() < MAX_TRANSACTIONS) {
                                    transfer(fromAccount, toAccount, sum);
                                    notSucsessfull = false;
                                } else {
                                    notSucsessfull = false;
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
                                if (quantityTransactions.get() < MAX_TRANSACTIONS) {
                                    transfer(fromAccount, toAccount, sum);
                                    notSucsessfull = false;
                                } else {
                                    notSucsessfull = false;
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
        }
        logger.info(String.format("Transaction complete with params: fromAccount %s toAccount %s sum %s",
                fromAccount, toAccount, sum));
    }

    private void transfer(Account fromAccount, Account toAccount, Long sum) {
        fromAccount.setBalance(fromAccount.getBalance() - sum);
        toAccount.setBalance(toAccount.getBalance() + sum);
    }
}
