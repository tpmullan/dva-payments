package com.praetorian.dva.rest;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommonResponse
{

    public static final String api = "";

    static public boolean isstatus(String response) throws JSONException
    {
        JSONObject json = new JSONObject(response);
        if (json.getString("status").equals("false"))
            return false;
        else
            return true;
    }
    
    static public HashMap<String,String> stdError(String error)
    {
        HashMap<String,String> errors = new HashMap<String,String>();
        errors.put("status", "false");
        errors.put("message", error);
        return errors;
    }

    static public HashMap<String, String> parseStatusAndErrors(String response)
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
                if (json.getString("status").equals("false"))
                {
                    results.put("status", "false");
                    try
                    {
                        JSONArray errorArray = json.getJSONArray("message");

                        for (int count = 0; count < errorArray.length(); count++)
                            errors += "-" + errorArray.getString(count).toString() + "\n\n";

                    } catch (JSONException e)
                    {
                        errors += json.getString("message");
                    }
                } else
                {
                    results.put("status", "true");
                    try
                    {
                        JSONArray errorArray = json.getJSONArray("message");

                        for (int count = 0; count < errorArray.length(); count++)
                            errors += "-" + errorArray.getString(count).toString() + "\n\n";

                    } catch (JSONException e)
                    {
                        errors += json.getString("message");
                    }
                }
            }
        } catch (JSONException e)
        {
            results.put("status", "false");
            results.put("message", e.getMessage());
        } catch (Exception e)
        {
            results.put("status", "false");
            results.put("message", e.getMessage());
        } finally
        {
            results.put("message", errors);
        }
        return results;
    }

    static public HashMap<String, String> parseStatusAndID(String response)
    {

        JSONObject json;
        HashMap<String, String> results = new HashMap<String, String>();
        String errors = "";

        try
        {
            json = new JSONObject(response);
            if (json.getString("status").equals("false"))
            {
                results.put("status", "false");
                try
                {
                    JSONArray array = json.getJSONArray("message");

                    for (int i = 0; i < array.length(); i++)
                    {
                        errors += array.getString(i).toString();
                    }

                } catch (JSONException e)
                {
                    errors += "-" + json.getString("message");
                }

                results.put("message", errors);
                return results;

            } else
            {
                results.put("status", "true");
                // if (json.getString("sessionToken").equals("")
                // || json.getString("sessionToken").equals("0")) {
                // results.put("sessionToken", "0");
                // return results;
                // } else {
                // results.put("sessionToken", json.getString("sessionToken"));
                results.put("userId", json.getString("userId"));

                JSONArray array = json.getJSONArray("message");
                for (int i = 0; i < array.length(); i++)
                {
                    errors += array.getString(i).toString() + "\n";
                }
                results.put("message", errors);
                return results;
                // }
            }

        } catch (JSONException e)
        {
            results.put("status", "false");
            results.put("message", e.getMessage());
            return results;
        } catch (Exception e)
        {
            results.put("status", "false");
            results.put("message", e.getMessage());
            return results;
        }
    }
}
