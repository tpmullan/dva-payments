package com.praetorian.dva.rest;

import java.util.HashMap;


import com.praetorian.dva.HTTPClient.RequestMethod;
import com.praetorian.dva.HTTPClient.RestClient;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.fragments.RegistrationFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

public class RegisterRequest extends CommonRequest{

	Context context;
	String host;
	private PrefStorage pref;
	public static final String API_PATH = "/users.json";
	public static final String POST_NAME = "user[name]";
	public static final String POST_USERNAME = "user[username]";
	public static final String POST_EMAIL = "user[email]";
	public static final String POST_PASSWORD1 = "user[password]";
	public static final String POST_PASSWORD2 = "user[password_confirmation]";
	
	

	public RegisterRequest(Context context) {

		this.context = context;
		pref = new PrefStorage(context);
		this.host = pref.getValue(Constants.KEY_SERVER_HOST);
		
	}

	
	public HashMap<String,String> registerUser(String fullname,String email,String username, String password,String password2) throws Exception
	{

		RestClient client = new RestClient(uri + host + api + API_PATH);
		client.AddParam(POST_NAME, fullname);
		client.AddParam(POST_EMAIL,email);
		client.AddParam(POST_USERNAME, username);
		client.AddParam(POST_PASSWORD1, password);
		client.AddParam(POST_PASSWORD2, password2);
		client.Execute(RequestMethod.POST, context);
		
		Log.println(Log.DEBUG, "network_json", fullname + " : " + email + " : " + username + " : " + password + " : " + password2 + "\n" + client.getResponse().toString());
		
		return RegisterResponse.parseStatusAndID(client.getResponse());
	}
}

