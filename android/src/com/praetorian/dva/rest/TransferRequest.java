package com.praetorian.dva.rest;

import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import com.praetorian.dva.HTTPClient.RequestMethod;
import com.praetorian.dva.HTTPClient.RestClient;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.fragments.RegistrationFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

public class TransferRequest extends CommonRequest
{

    Context context;
    String host;
    private PrefStorage pref;
    public static final String API_PATH = "/transfer.json";
    public static final String API_PATH_TOKEN = "/token.json";

    public static final String POST_TOACCOUNT = "transfer[toAccount]";
    public static final String POST_FROMACCOUNT = "transfer[fromAccount]";
    public static final String POST_AMOUNT = "transfer[amount]";
    private static final String POST_TOKEN = "token";

    private static final String POST_TOKEN_FROMACCOUNT = "token[account_number]";
    private static final String POST_TOKEN_AMOUNT = "token[amount]";
    private static final String PUT_TOKEN_TOACCOUNT = "account";
    private static final String PUT_TOKEN_TOKEN = "token";

    public TransferRequest(Context context)
    {

        this.context = context;
        pref = new PrefStorage(context);
        this.host = pref.getValue(Constants.KEY_SERVER_HOST);

    }

    public HashMap<String, String> sendTransfer(String toAccount, String fromAccount, double amount) throws Exception
    {
        RestClient client = new RestClient(uri + host + api + API_PATH);

        client.AddParam(POST_AMOUNT, "" + amount);
        client.AddParam(POST_TOACCOUNT, toAccount);
        if (!fromAccount.equals("")) client.AddParam(POST_FROMACCOUNT, fromAccount);

        client.Execute(RequestMethod.POST, context);

        Log.d("network_json", client.getResponse());

        return TransactionResponse.parseStatusAndErrors(client.getResponse());

    }

    public HashMap<String, String> getToken(String fromAccount, String amount) throws Exception
    {
        RestClient client = new RestClient(uri + host + api + API_PATH_TOKEN);
        
        
        if(!fromAccount.equals("")) client.AddParam(POST_TOKEN_FROMACCOUNT, fromAccount);
        client.AddParam(POST_TOKEN_AMOUNT, "" + amount);
        HashMap<String, String> output = null;

        try
        {
            client.Execute(RequestMethod.POST, context);
            Log.d("network_json", "getToken" + client.getResponse());
            output = TransferResponse.parseStatusandToken(client.getResponse());
        } catch (ClientProtocolException e)
        {
            output = LoginResponse.stdError("Server Error, Please try again later");
        }
        return output;
    }

    public HashMap<String, String> sendToken(String token, String toAccount) throws Exception
    {
        RestClient client = new RestClient(uri + host + api + API_PATH_TOKEN);
        client.AddParam(PUT_TOKEN_TOKEN, token);
        
        if (!toAccount.equals("")) client.AddParam(PUT_TOKEN_TOACCOUNT, toAccount);

        client.Execute(RequestMethod.PUT, context);

        Log.d("network_json", client.getResponse());

        return TransactionResponse.parseStatusAndErrors(client.getResponse());

    }
    

}
