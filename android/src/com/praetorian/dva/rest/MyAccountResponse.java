package com.praetorian.dva.rest;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAccountResponse extends CommonResponse
{

    public static final String KEY_SESSION_TOKEN = "loginResponse_sessionId";
    public static final String KEY_SESSION_TOKEN_EXP = "loginResponse_sessionIdExp";

    public static HashMap<String, String> parseAccount(String result)
    {
        JSONObject json;
        HashMap<String, String> results = new HashMap<String, String>();
        String errors = "";

        try
        {
            json = new JSONObject(result);

            results.put(MyAccountRequest.POST_EMAIL, json.getString("email"));
            results.put(MyAccountRequest.POST_USERNAME, json.getString("username"));
            results.put("status", "true");

        } catch (JSONException e)
        {

            results.put("status", "false");
            results.put("message", e.toString());
        }

        return results;

    }

}
