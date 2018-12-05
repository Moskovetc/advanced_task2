package utilities;

import accounts.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateRandom {
    private List<String> accountNameList = Arrays.asList("Vasya", "Kolya", "Vova", "Nikita", "Dima", "Mike", "Michel",
            "John", "Arnold", "Putin", "Valera", "Azat");
    private final int MAX_ID = 10000;
    private final int MAX_BALANCE = 10000000;
    Random random = new Random();

    public Account getAccount() {
        Account account = new Account();
        account.setAccountName(accountNameList.get(random.nextInt(accountNameList.size())));
        account.setId(getId());
        account.setBalance(getSum(MAX_BALANCE));
        return account;
    }

    public Long getId (){
        return (long) random.nextInt(MAX_ID);
    }

    public Long getSum(int sum){
        return (long) random.nextInt(sum);
    }
}
