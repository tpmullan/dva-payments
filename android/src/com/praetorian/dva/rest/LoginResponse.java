package com.praetorian.dva.rest;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse extends CommonResponse
{

    public static final String KEY_SESSION_TOKEN = "loginResponse_sessionId";
    public static final String KEY_SESSION_TOKEN_EXP = "loginResponse_sessionIdExp";
    public static final String KEY_USERID = "userId";

}
