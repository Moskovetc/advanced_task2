package service;

import accounts.Account;
import java.util.concurrent.locks.ReentrantLock;

public class Transaction {
    private Account fromAccount;
    private Account toAccount;
    private ReentrantLock lock;
}
