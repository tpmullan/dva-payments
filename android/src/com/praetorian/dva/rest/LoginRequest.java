package com.praetorian.dva.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;


import com.praetorian.dva.HTTPClient.RequestMethod;
import com.praetorian.dva.HTTPClient.RestClient;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

public class LoginRequest extends CommonRequest{

	Context context;
	String host;
	private PrefStorage pref;

	
	public static final String API_PATH = "/sessions.json";
	public static final String POST_USERNAME = "session[email]";
	public static final String POST_PASSWORD = "session[password]";

	
	

	public LoginRequest(Context context) {

		this.context = context;
		pref = new PrefStorage(context);
		this.host = pref.getValue(Constants.KEY_SERVER_HOST);
		
		
	}

	
	public HashMap<String,String> authenticateUser(String username, String password) throws UnsupportedEncodingException
	{
	    HashMap<String,String> output = null;
		RestClient client = new RestClient(uri + host + api + API_PATH);
		client.AddParam(POST_USERNAME, username);
		client.AddParam(POST_PASSWORD,password);
		
		
		try{
		    client.Execute(RequestMethod.POST, context);
		    Log.println(Log.DEBUG, "network_json", client.getResponse().toString());
	        
	        output = LoginResponse.parseStatusAndID(client.getResponse());
		}
		catch(ClientProtocolException e)
		{
		    output = LoginResponse.stdError("Server Error, Please try again later");
		}
		catch(IOException e)
		{
		    output = LoginResponse.stdError("Error, Please try again later");
		}
		
		return output;
		
	}
}

