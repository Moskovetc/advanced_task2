package dao;

import accounts.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAO implements IDataAccessObject {
    private static final String path = "src/main/resources/accounts/";
    private static final String PATH_PREFIX = "account";
    private static final Logger logger = LoggerFactory.getLogger(DAO.class);

    public Account get(String accountName) {
        try {
            FileInputStream fileIn = new FileInputStream(path + accountName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Account account = (Account) objectIn.readObject();
            objectIn.close();
            logger.debug(String.format("Started method get with account name: %s geted: %s", accountName, account));
            return account;
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Account %s not found", accountName));
            return null;
        } catch (IOException e) {
            logger.error(String.format("Account %s locked by another process. File was opened in another program", accountName));
            return null;
        } catch (ClassNotFoundException e) {
            logger.error(String.format("File %s is not contain an Account", accountName));
            return null;
        }
    }

    public void set(Account account) {
        String accountName = PATH_PREFIX + account.getId();
        try {
            FileOutputStream fileOut = new FileOutputStream(path + accountName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(account);
            objectOut.close();
            logger.debug(String.format("Started method set with account name: %s seted: %s", accountName, account));
        } catch (FileNotFoundException e) {
            logger.error(String.format("File %s not found", accountName));
        } catch (IOException e) {
            logger.error(String.format("Account %s locked by another process. File was opened in another program", accountName));
        }

    }

    public List<String> getAccountNames() {
        File folder = new File(path);
        List<String> accountNames = new ArrayList<String>();

        if (folder.isDirectory()) {
            if (null != folder.list()) {
                accountNames.addAll(Arrays.asList(folder.list()));
            }
        }
        logger.debug(String.format("Started method getAccountNames, account names geted: %s", accountNames));
        return accountNames;
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        for(String accountName : getAccountNames()) {
            accounts.add(get(accountName));
        }
        logger.debug(String.format("Started method getAccounts, accounts geted: %s", accounts));
        return accounts;
    }

    public void deleteAll() {
        File folder = new File(path);
        if (folder.isDirectory()) {
            if (null != folder.list()) {
                List<String> files = Arrays.asList(folder.list());
                for (String file : files) {
                    File myFile = new File(folder, file);
                    myFile.delete();
                }
            }
        }
    }
}
