package com.praetorian.dva.rest;

import java.io.IOException;
import java.util.HashMap;


import com.praetorian.dva.HTTPClient.RequestMethod;
import com.praetorian.dva.HTTPClient.RestClient;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.fragments.RegistrationFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

public class TransactionRequest extends CommonRequest{

	Context context;
	String host;
	private PrefStorage pref;
	public static final String API_PATH = "/transactions.json";
	public static final String API_PATH_ACCOUNT_TRANS = "/transactions/";
	public static final String API_PATH_BALANCE = "/balance.json";
	
	
	

	public TransactionRequest(Context context) {

		this.context = context;
		pref = new PrefStorage(context);
		this.host = pref.getValue(Constants.KEY_SERVER_HOST);
		
	}
	public HashMap<String,Object>  getTransactions(String userId) throws Exception
	{
		RestClient client = new RestClient(uri + host + api + API_PATH);
		
		client.Execute(RequestMethod.GET, context);
		
		Log.d("network_json_transactions",client.getResponse());
		
		return TransactionResponse.parseTransactions(client.getResponse());
		
	}
	public HashMap<String,Object>  getTransactions(String userId,int Account) throws Exception
    {
        RestClient client = new RestClient(uri + host + api + API_PATH_ACCOUNT_TRANS + Account + ".json");
        
        client.Execute(RequestMethod.GET, context);
        
        Log.d("network_json_transactions",client.getResponse());
        
        return TransactionResponse.parseTransactions(client.getResponse());
        
    }
	public HashMap<String,String> getBalance() throws Exception
	{
		RestClient client = new RestClient(uri + host + api + API_PATH_BALANCE);
		
		client.Execute(RequestMethod.GET, context);
		
		Log.d("network_json_balance",client.getResponse());
		
		return TransactionResponse.parseBalance(client.getResponse());
	}
    public HashMap<String, Object> getAccounts() throws IllegalStateException, IOException
    {
        RestClient client = new RestClient(uri + host + api + API_PATH_BALANCE);
        
        client.Execute(RequestMethod.GET, context);
        
        Log.d("network_json_accounts",client.getResponse());
        
        return TransactionResponse.parseAccounts(client.getResponse());
    }
}

