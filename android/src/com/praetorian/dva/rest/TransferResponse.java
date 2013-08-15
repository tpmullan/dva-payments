package com.praetorian.dva.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.JSONObject;

import com.praetorian.dva.misc.Transaction;

public class TransferResponse extends CommonResponse
{
    public static HashMap<String, String> parseStatusandToken(String response)
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

                if (json.getBoolean("status"))
                {
                    results.put("status", "true");

                    results.put("token", json.getString("token"));
                } else
                {
                    results.put("status", "false");
                    results.put("message", "unable to create qr code");
                }
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
