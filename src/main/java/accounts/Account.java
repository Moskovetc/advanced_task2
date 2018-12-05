package accounts;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;


public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountName;
    private Long id;
    private Long balance;
    private ReentrantLock lock = new ReentrantLock();


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountName='" + accountName + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
