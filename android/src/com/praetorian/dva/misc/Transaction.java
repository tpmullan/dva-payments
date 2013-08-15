package com.praetorian.dva.misc;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;

/**
 * Transaction Object Contains all of the information for the transaction object
 * for the list view
 * 
 * @author richard
 * 
 */
public class Transaction
{
    private Date time;
    private String toAccount;
    private String fromAccount;
    private double amount;
    private String memo;
    private double balance;

    public Transaction()
    {
        time = new Date();
        toAccount = "" + (int) (Math.random() * 1000000);
        fromAccount = "" + (int) (Math.random() * 100000);
        amount = (Math.random() * 10000);
        memo = "Dummy Record";
        balance = amount + 300;
    }

    public Transaction(String time, String toAccount, String fromAccount, String amount, String memo, String balance)
    {
        this.time = new Date();

        final String pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'";
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try
        {
            this.time = sdf.parse(time);
        } catch (java.text.ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.amount = Double.parseDouble(amount);
        this.memo = memo;
        this.balance = Double.parseDouble(balance);
    }

    public double getBalance()
    {
        return balance;
    }

    public Date getTime()
    {
        return time;
    }

    public String getToAccount()
    {
        return toAccount;
    }

    public String getFromAccount()
    {
        return fromAccount;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getMemo()
    {
        return memo;
    }

}
