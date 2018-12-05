package accounts;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountName;
    private Long id;
    private Long balance;

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

    @Override
    public String toString() {
        return "Account{" +
                "accountName='" + accountName + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
