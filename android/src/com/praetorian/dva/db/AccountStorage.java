package com.praetorian.dva.db;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


import com.praetorian.dva.misc.Account;
import com.praetorian.dva.rest.TransactionRequest;

import android.content.Context;
import android.os.AsyncTask;
/**
 * Damn Vulnerable Application
 * 
 * Account Storage stores account numbers into a the prefs storage database.
 * 
 * @author Richard Penshorn
 * Praetorian Labs 2013
 */
public class AccountStorage
{
    private static ArrayList<Account> accounts;
    private static final String KEY_ACCOUNT_LIST = "accountnumbers";
    private Context context;
    public AccountStorage(Context context)
    {
        if(accounts == null)
            accounts = new ArrayList<Account>();
        
        this.context = context;
    }
    /**
     * Checks if account exist already
     * @param account (Account to be checked)
     * @return if account exist or not
     */
    public boolean hasAccount(String account)
    {
        for(int k = 0; k < accounts.size(); k++)
        {
            if(accounts.get(k).getAccountNumber().equals(account))
                return true;
        }
        return false;
    }
    /**
     * Adds an account into the account storage
     * @param account
     */
    public void addAccount(Account account)
    {
       if(!hasAccount(account.getAccountNumber()))
        accounts.add(account);
    }
    /**
     * Returns all current account in the storage
     * @return String array of accounts
     */
    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }
    /**
     * Deletes accounts stored in the account storage
     */
    public void destory()
    {
        accounts.clear();
        
    }

    public void updateAccounts()
    {
    	TransactionRequest client = new TransactionRequest(context);
    	HashMap<String, Object> success = null;
    	 try
         {
             success = client.getAccounts();

         } catch (Exception e)
         {
             success = new HashMap<String, Object>();
             success.put("status", "false");
             success.put("message", e.toString());
         }
    	 
    	 if (((String) success.get("status")).equals("true"))
         {
             @SuppressWarnings("unchecked")
			ArrayList<Account> accounts = (ArrayList<Account>) success.get("accounts");

             for(int k = 0; k < accounts.size(); k++)
             {
                 addAccount(accounts.get(k));
             }

         }
    }

}
