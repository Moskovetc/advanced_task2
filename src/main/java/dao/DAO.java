package dao;

import accounts.Account;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DAO implements IDataAccessObject {
    private final String path = "src\\main\\resources\\";
    public static final String PATH_PREFIX = "account";

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

    public void deleteAll() {
        File file = new File(path);
        if (file.isDirectory()) {
            if (null != file.list()) {
                List<String> files = Arrays.asList(file.list());
                for (String file1 : files) {
                    File myFile = new File(file, file1);
                    myFile.delete();
                }
            }
        }
    }
}
