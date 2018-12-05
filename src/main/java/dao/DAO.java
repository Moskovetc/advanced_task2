package dao;

import accounts.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAO implements IDataAccessObject {
    private final String path = "src/main/resources/";
    private final String PATH_PREFIX = "account";

    public Account get(String accountName) {
        try {
            FileInputStream fileIn = new FileInputStream(path + accountName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Account account = (Account) objectIn.readObject();
            objectIn.close();
            return account;
        } catch (FileNotFoundException e) {
            System.err.println("Account not found");
            return null;
        } catch (IOException e) {
            System.err.println("Account locked");
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("File is not contain an Account");
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
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Account locked");
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
        return accountNames;
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        for(String accountName : getAccountNames()) {
            accounts.add(get(accountName));
        }
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
