package com.praetorian.dva.HTTPClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.praetorian.dva.db.PrefStorage;

import android.content.Context;
import android.util.Log;
/**
 * Handles cookies for the rest client. It will add and update cookies when the server request thru the REST client.  
 * @author richard
 *
 */
public class CookieClient
{
    public static final String KEY_COOKIE = "cookieclient_values_";
    public static final String KEY_COOKIE_LIST = "cookieclient_keylist";
    private Context context;
    private PrefStorage prefs;
    private Set<String> cookieHash;

    public CookieClient(Context context)
    {
        prefs = new PrefStorage(context);
        this.context = context;
        cookieHash = new HashSet<String>();
        loadKeys();
    }
    /**
     * Loads up keys from the Pref Storage (cookiekeys)
     */
    private void loadKeys()
    {
        String keys = prefs.getValue(KEY_COOKIE_LIST);
        String keysArray[] = keys.split(";");
        if (keys.equals(PrefStorage.NULL)) return;
        for (String s : keysArray)
        {
            cookieHash.add(s);
        }
    }
    /**
     * Updates/Creates cookie for the cookie client
     * @param name keyvalue of the cookie
     * @param value of the cookie
     */
    public void setCookie(String name, String value)
    {

        String[] values = value.split("[;=]");
        String key = values[0];
        String data = values[1];

        cookieHash.add(key);

        prefs.updateValue(KEY_COOKIE + key, data);

        String keySet = "";
        for (String s : cookieHash)
        {
            keySet += s + ";";
        }

        prefs.updateValue(KEY_COOKIE_LIST, keySet);

    }
    /**
     * Returns a string of cookies which can be used to append to the header. 
     * @return
     */
    public String sendCookies()
    {
        String output = "";
        for (String s : cookieHash)
        {
            output += s + "=" + prefs.getValue(KEY_COOKIE + s) + ";";
        }
        return output;
    }

}
