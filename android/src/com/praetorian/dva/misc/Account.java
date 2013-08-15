package com.praetorian.dva.misc;

/**
 * Account object which holds account information
 * 
 * @author Richard Penshorn
 * 
 */
public class Account
{
    private String accountNumber;
    private String accountName;
    private double accountBalance;
    private boolean externalAccount = false;
    private int internalAccountId = -1;

    public Account(String number, String name, double balance)
    {
        accountNumber = number;
        accountName = number;
        accountBalance = balance;
        externalAccount = false;
        internalAccountId = -1;
    }
    public Account(String number, String name, double balance,int id)
    {
        accountNumber = number;
        accountName = name;
        accountBalance = balance;
        externalAccount = false;
        internalAccountId = id;
    }

    public Account(String number, String name, double balance, boolean external)
    {
        accountNumber = number;
        accountName = number;
        accountBalance = balance;
        externalAccount = external;
    }
    public Account(String number, String name, double balance, boolean external, int id)
    {
        accountNumber = number;
        accountName = number;
        accountBalance = balance;
        externalAccount = external;
        internalAccountId = id;
    }

    public Account(String number, double balance)
    {
        accountNumber = number;
        accountName = "Savings Account";
        accountBalance = balance;
        internalAccountId = -1;
    }
    public Account(String number, double balance, int id)
    {
        accountNumber = number;
        accountName = "Savings Account";
        accountBalance = balance;
        internalAccountId = id;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public double getAccountBalance()
    {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance)
    {
        this.accountBalance = accountBalance;
    }
    public int getId()
    {
        return internalAccountId;
    }
    public String toString()
    {
        if (externalAccount)
        {
            return accountName + " (" + accountNumber + ")";
        } else
        {
            if (accountNumber.length() < 4) return "";
            return accountName + " (" + accountNumber.substring(accountNumber.length() - 5) + ") $" + accountBalance;
        }
    }
    public String getFriendlyName()
    {
        String number = "";
        if(accountNumber.length() > 4)
        {
            number = accountNumber.substring(accountNumber.length() -4);
        }
        else
        {
            number = accountNumber;
        }
        
        return accountName + "(" + number + ")";
        
        
    }

}
