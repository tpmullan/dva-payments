package com.praetorian.dva.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.praetorian.dva.HTTPClient.RequestMethod;
import com.praetorian.dva.HTTPClient.RestClient;
import com.praetorian.dva.db.Contact;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.fragments.RegistrationFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

public class ContactCheckRequeset extends CommonRequest
{

    Context context;
    String host;
    private PrefStorage pref;
    private String API_PATH_BULK_CREATE = "/friends.json";
    private String API_PATH_BULK_SHOW = "/friends.json";
    private String POST_EMAILS = "";
    public static final String API_PATH = "/users/valid.json";

    public static final String POST_EMAIL = "email";

    public ContactCheckRequeset(Context context)
    {

        this.context = context;
        pref = new PrefStorage(context);
        this.host = pref.getValue(Constants.KEY_SERVER_HOST);

    }

    public boolean checkEmail(String email) throws Exception
    {
        RestClient client = new RestClient(uri + host + api + API_PATH);

        client.AddParam(POST_EMAIL, email);
        Log.d("network_json", uri + host + api + API_PATH);

        client.Execute(RequestMethod.POST, context);

        Log.d("network_json", client.getResponse());

        return ContactCheckResponse.parseStatus(client.getResponse());

    }

    public boolean checkEmailBulk(String[] emails) throws Exception
    {
        RestClient client = new RestClient(uri + host + api + API_PATH_BULK_CREATE);
        Log.d("network_json", uri + host + api + API_PATH_BULK_CREATE);
        JSONArray jarray = new JSONArray();

        for (int k = 0; k < emails.length; k++)
        {
            jarray.put(emails[k]);
        }
        JSONObject jobj = new JSONObject();
        jobj.put("emails", jarray);
        Log.d("network_json", jobj.toString());
        
        client.setBody(jobj.toString());
        client.setContentType("application/json");
        client.Execute(RequestMethod.POST, context);

        Log.d("network_json", client.getResponse());

        return ContactCheckResponse.parseStatus(client.getResponse());

    }

    public ArrayList<Contact> getFriends() throws IllegalStateException, IOException
    {
        
        
        RestClient client = new RestClient(uri + host + api + API_PATH_BULK_SHOW);
        
        
        
        client.Execute(RequestMethod.GET,context);
        
        Log.v("client_contact","Getting Friends" + client.getResponse().toString());
        
        return ContactCheckResponse.parseFriends(client.getResponse());

    }

}
