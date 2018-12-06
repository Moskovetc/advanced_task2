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
    private final int MAX_TRANSACTIONS = 1000;
    private final Long MAX_SUM = 10000000L;
    private GenerateRandom generateRandom = new GenerateRandom();
    private List<Account> accounts;
    private final Logger logger = LoggerFactory.getLogger(MoneyTransfer.class);

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
                transaction(fromAccount, toAccount, sum);
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
                    logger.warn(String.format("%s Accounts from & to are equals. Account ID: %s", Thread.currentThread().getName(), fromAccount.getId()));
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

    private void transaction(Account fromAccount, Account toAccount, Long sum) {
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
