package com.praetorian.dva.rest;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.JSONObject;

import com.praetorian.dva.db.Contact;
import com.praetorian.dva.misc.Transaction;

public class ContactCheckResponse extends CommonResponse
{

    public static boolean parseStatus(String response)
    {
        HashMap<String, String> output = CommonResponse.parseStatusAndErrors(response);

        if (output.get("status") != "")
        {
            return Boolean.parseBoolean(output.get("status"));
        }
        return false;
    }

    public static ArrayList<Contact> parseFriends(String response)
    {
        JSONArray arr;
        ArrayList<Contact> output = new ArrayList<Contact>();
        try
        {
            arr = new JSONArray(response);
            for (int k = 0; k < arr.length(); k++)
            {
                JSONObject obj;
                obj = arr.getJSONObject(k);

                output.add(new Contact(obj.getString("name"), obj.getString("username"), obj.getString("email")));

            }
            
            return output;
        } catch (JSONException e)
        {
            return output;
        }

    }
}
