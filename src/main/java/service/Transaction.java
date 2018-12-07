package service;

import accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transaction {
    private Account fromAccount;
    private Account toAccount;
    private Long sum;
    private int sleepTimer;

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", sum=" + sum +
                '}';
    }

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Account fromAccount, Account toAccount, Long sum, int sleepTimer) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.sum = sum;
        this.sleepTimer = sleepTimer;
    }

    public boolean complete() {
        boolean sucsessfull = false;
        while (!sucsessfull) {
            if (fromAccount.getId() < toAccount.getId()) {
                if (fromAccount.getLock().tryLock()) {
                    try {
                        if (toAccount.getLock().tryLock()) {
                            try {
                                sleep(sleepTimer);
                                transfer(fromAccount, toAccount, sum);
                                sucsessfull = true;
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
                                sleep(sleepTimer);
                                transfer(fromAccount, toAccount, sum);
                                sucsessfull = true;
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
        return sucsessfull;
    }

    private void transfer(Account fromAccount, Account toAccount, Long sum) {
        fromAccount.setBalance(fromAccount.getBalance() - sum);
        toAccount.setBalance(toAccount.getBalance() + sum);
    }

    private void sleep(int sleepTimer) {
        try {
            Thread.sleep(sleepTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
