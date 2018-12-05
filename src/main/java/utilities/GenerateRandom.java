package utilities;

import accounts.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateRandom {
    private List<String> accountNameList = Arrays.asList("Vasya", "Kolya", "Vova", "Nikita", "Dima", "Mike", "Michel",
            "John", "Arnold", "Putin", "Valera", "Azat");
    private final Long MAX_ID = 10000L;
    private final Long MAX_BALANCE = 10000000L;
    Random random = new Random();

    public Account getAccount() {
        Account account = new Account();
        account.setAccountName(accountNameList.get(random.nextInt(accountNameList.size())));
        account.setId(getId());
        account.setBalance(getSum(MAX_BALANCE));
        return account;
    }

    private Long randomLong(Long maximum){
        long min = 0L;
        long max = maximum;
        return min + (long) (Math.random() * max);
    }

    public Long getId (){
        return randomLong(MAX_ID);
    }

    public Long getSum(Long sum){
        return randomLong(sum);
    }
}
