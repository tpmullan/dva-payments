package com.praetorian.dva.rest;

import java.util.HashMap;


import com.praetorian.dva.HTTPClient.RequestMethod;
import com.praetorian.dva.HTTPClient.RestClient;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.MyAccountFragment;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

public class MyAccountRequest extends CommonRequest{

	Context context;
	String host;
	private PrefStorage pref;
	private String userId;

	
	public static final String API_PATH = "/users/";
	public static final String POST_EMAIL = "user[email]";
	public static final String POST_USERNAME = "user[username]";
	public static final String POST_PASSWORD = "user[password]";
	public static final String POST_PASSWORD2 = "user[password_confirmation]";

	
	

	public MyAccountRequest(Context context) {

		this.context = context;
		pref = new PrefStorage(context);
		this.host = pref.getValue(Constants.KEY_SERVER_HOST);
		
		userId = pref.getValue(LoginResponse.KEY_USERID);
		
		
		
	}

	
	public HashMap<String,String> getUser(String userId) throws Exception
	{
		RestClient client = new RestClient(uri + host + api + API_PATH + userId + ".json");
		client.Execute(RequestMethod.GET, context);
		
		Log.println(Log.DEBUG, "network_json", client.getResponse().toString());
		
		return MyAccountResponse.parseAccount(client.getResponse());
	}

	public HashMap<String,String> updateUser(HashMap<String,String> params, String userId) throws Exception
	{
		RestClient client = new RestClient(uri + host + api + API_PATH + userId + ".json");
		
		if(params.containsKey(POST_EMAIL))
		{
			client.AddParam(POST_EMAIL, params.get(POST_EMAIL));
		}
		if(params.containsKey(POST_USERNAME))
		{
			client.AddParam(POST_USERNAME, params.get(POST_USERNAME));
		}
		if(params.containsKey(POST_PASSWORD))
		{
			client.AddParam(POST_PASSWORD, params.get(POST_PASSWORD));
		}
		if(params.containsKey(POST_PASSWORD2))
		{
			client.AddParam(POST_PASSWORD2, params.get(POST_PASSWORD2));
		}
		client.Execute(RequestMethod.PUT, context);
		
		Log.d("network_json",uri + host + api + API_PATH + userId + ".json" + "\n" + client.getResponse());
		return MyAccountResponse.parseStatusAndErrors(client.getResponse());
	}
	
	public HashMap<String,String> deleteUser(String userId) throws Exception
	{
		RestClient client = new RestClient(uri + host + api + API_PATH + userId + ".json");
		
		client.Execute(RequestMethod.DELETE, context);
		
		Log.d("network_json",uri + host + api + API_PATH + userId + ".json" + "\n" + client.getResponse());
		

		return MyAccountResponse.parseStatusAndErrors(client.getResponse());
	}
}

