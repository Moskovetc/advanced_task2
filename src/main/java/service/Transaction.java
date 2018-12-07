package service;

import accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Transaction {
    private Account fromAccount;
    private Account toAccount;
    private Long sum;
    private Random random = new Random();
    private static final int MIN_TIME_TO_SLEEP = 500;
    private static final int RANDOM_TIME_TO_SLEEP = 500;

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", sum=" + sum +
                '}';
    }

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Account fromAccount, Account toAccount, Long sum) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.sum = sum;
    }

    public boolean complete() {
        boolean sucsessfull = false;
        while (!sucsessfull) {
            if (fromAccount.getLock().tryLock()) {
                try {
                    if (toAccount.getLock().tryLock()) {
                        try {
                            randomSleep();
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
        logger.info(String.format("Transaction complete with params: fromAccount %s toAccount %s sum %s",
                fromAccount, toAccount, sum));
        return sucsessfull;
    }

    private void transfer(Account fromAccount, Account toAccount, Long sum) {
        fromAccount.setBalance(fromAccount.getBalance() - sum);
        toAccount.setBalance(toAccount.getBalance() + sum);
    }

    private void randomSleep() {
        if (0 < Transaction.MIN_TIME_TO_SLEEP && 0 < Transaction.RANDOM_TIME_TO_SLEEP) {
            try {
                Thread.sleep(Transaction.MIN_TIME_TO_SLEEP + random.nextInt(Transaction.RANDOM_TIME_TO_SLEEP));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
