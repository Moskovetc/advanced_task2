package utilities;

import accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateRandom {
    private List<String> accountNameList = Arrays.asList("Vasya", "Kolya", "Vova", "Nikita", "Dima", "Mike", "Michel",
            "John", "Arnold", "Putin", "Valera", "Azat");
    private final Long MAX_ID = 10000L;
    private final Long MAX_BALANCE = 10000000L;
    Random random = new Random();
    private final Logger logger = LoggerFactory.getLogger(GenerateRandom.class);

    public Account getAccount() {
        Account account = new Account();
        account.setAccountName(accountNameList.get(random.nextInt(accountNameList.size())));
        account.setId(getId());
        account.setBalance(getSum(MAX_BALANCE));
        logger.debug(String.format("Started method getAccount, account geted: %s", account));
        return account;
    }

    private Long randomLong(Long maximum) {
        long min = 0L;
        long max = maximum;
        long randomLong = min + (long) (Math.random() * max);
        logger.debug(String.format("Started method randomLong, random Long generated: %s", randomLong));
        return randomLong;
    }

    public Long getId() {
        logger.debug(String.format("Started method getId"));
        return randomLong(MAX_ID);
    }

    public Long getSum(Long sum) {
        logger.debug(String.format("Started method getSum"));
        return randomLong(sum);
    }
}
