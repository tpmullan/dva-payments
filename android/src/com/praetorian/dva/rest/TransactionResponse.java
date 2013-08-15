package com.praetorian.dva.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.praetorian.dva.misc.Account;
import com.praetorian.dva.misc.Transaction;

public class TransactionResponse extends CommonResponse
{

    public static final String KEY_TIME = "created_at";
    public static final String KEY_TO_ACCOUNT = "toAccount";
    public static final String KEY_FROM_ACCOUNT = "fromAccount";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_MEMO = "memo";
    public static final String KEY_BALANCE = "balance";

    public static HashMap<String, Object> parseTransactions(String response)
    {
        JSONObject json;
        HashMap<String, Object> results = new HashMap<String, Object>();
        String errors = "";

        try
        {
            if (response == null)
            {
                errors += "Non Connecting";
                results.put("status", "false");
            } else
            {
                json = new JSONObject(response);
                results.put("status", "true");
                ArrayList<Transaction> trans = new ArrayList<Transaction>();
                JSONArray transArray = json.getJSONArray("transactions");

                for (int i = 0; i < transArray.length(); i++)
                {
                    JSONObject tran = (JSONObject) transArray.get(i);

                    String time = tran.getString(KEY_TIME);
                    String toAccount = tran.getString(KEY_TO_ACCOUNT);
                    String fromAccount = tran.getString(KEY_FROM_ACCOUNT);
                    String amount = tran.getString(KEY_AMOUNT);
                    String memo = tran.getString(KEY_MEMO);
                    String balance = tran.getString(KEY_BALANCE);

                    trans.add(new Transaction(time, toAccount, fromAccount, amount, memo, balance));
                }

                results.put("transactions", trans);
            }
        } catch (JSONException e)
        {
            results.put("status", "false");
            errors += "JSONException\n" + e.getMessage();
        } catch (Exception e)
        {
            results.put("status", "false");
            errors += "Exception \n" + Arrays.toString(e.getStackTrace());
        } finally
        {
            results.put("message", errors);
        }
        return results;
    }

    public static HashMap<String, String> parseBalance(String response)
    {
        JSONObject json;
        HashMap<String, String> results = new HashMap<String, String>();
        String errors = "";

        try
        {
            if (response == null)
            {
                errors += "Non Connecting";
                results.put("status", "false");
            } else
            {
                json = new JSONObject(response);

                JSONArray jsonArray = json.getJSONArray("accounts");

                results.put("status", "true");

                results.put("balance", jsonArray.getJSONObject(0).getString("balance"));
                results.put("account", jsonArray.getJSONObject(0).getString("number"));
            }
        } catch (JSONException e)
        {
            results.put("status", "false");
            errors += "JSONException\n" + e.getMessage();
        } catch (Exception e)
        {
            results.put("status", "false");
            errors += "Exception \n" + Arrays.toString(e.getStackTrace());
        } finally
        {
            results.put("message", errors);
        }
        return results;

    }

    public static HashMap<String, Object> parseAccounts(String response)
    {
        JSONObject json;
        HashMap<String, Object> results = new HashMap<String, Object>();
        String errors = "";
        ArrayList<Account> accounts = new ArrayList<Account>();

        try
        {
            if (response == null)
            {
                errors += "Non Connecting";
                results.put("status", "false");
            } else
            {
                json = new JSONObject(response);

                JSONArray jsonArray = json.getJSONArray("accounts");

                results.put("status", "true");

                for (int k = 0; k < jsonArray.length(); k++)
                {
                    accounts.add(new Account(jsonArray.getJSONObject(k).getString("number"),
                            jsonArray.getJSONObject(k).getString("name"),
                            Double.parseDouble(jsonArray.getJSONObject(k).getString("balance")),
                            jsonArray.getJSONObject(k).getInt("id")));
                }
                
                results.put("accounts",accounts);
            }
        } catch (JSONException e)
        {
            results.put("status", "false");
            errors += "JSONException\n" + e.getMessage();
        } catch (Exception e)
        {
            results.put("status", "false");
            errors += "Exception \n" + Arrays.toString(e.getStackTrace());
        } finally
        {
            results.put("message", errors);
        }
        return results;
    }

}
