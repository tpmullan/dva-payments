package com.praetorian.dva.HTTPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.ProxyFragment;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.util.Log;

/* credit goes to: 
 * http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/
 * for this clean to implement and handy class
 */
public class RestClient
{

    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;
    private String url;
    private int responseCode;
    private String message;
    private String response;
    private PrefStorage prefs;
    private CookieClient cookies;
    private String encoding;
    private String body;

    public String getResponse()
    {
        return response;
    }

    public String getErrorMessage()
    {
        return message;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public RestClient(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();

    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }
    public void setContentType(String type)
    {
        
       headers.add(new BasicNameValuePair("Content-Type",type));       
    }
    public void setBody(String body)
    {
        this.body = body;
    }

    public void Execute(RequestMethod method, Context context) throws IllegalStateException, IOException
    {
        cookies = new CookieClient(context);
        switch (method)
        {
        case GET:
        {
            // add parameters
            String combinedParams = "";
            if (!params.isEmpty())
            {
                combinedParams += "?";
                for (NameValuePair p : params)
                {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                    if (combinedParams.length() > 1)
                    {
                        combinedParams += "&" + paramString;
                    } else
                    {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);
            

            
            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }
            request.addHeader("Cookie", cookies.sendCookies());
            Log.d("cookie_client", cookies.sendCookies());

            executeRequest(request, url, context);
            break;
        }
        case POST:
        {
            HttpPost request = new HttpPost(url);
            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            request.addHeader("Cookie", cookies.sendCookies());
            Log.d("cookie_client", cookies.sendCookies());
            
            if(encoding != null)
            {
            StringEntity se = new StringEntity("");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,encoding));
            request.setEntity(se);
            }
            

            if (!params.isEmpty())
            {
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            else
            {
                request.setEntity(new ByteArrayEntity(body.toString().getBytes("UTF8")));
            }

            executeRequest(request, url, context);
            break;
        }
        case DELETE:
        {
            HttpDelete request = new HttpDelete(url);

            // add headers
            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }


            request.addHeader("Cookie", cookies.sendCookies());

            executeRequest(request, url, context);
            break;
        }
        case PUT:
        {
            HttpPut request = new HttpPut(url);

            for (NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if(encoding != null)
            {
            StringEntity se = new StringEntity("");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,encoding));
            request.setEntity(se);
            }
            
            request.addHeader("Cookie", cookies.sendCookies());

            if (!params.isEmpty())
            {
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            Log.d("network_json", Arrays.toString(params.toArray()));
            executeRequest(request, url, context);
            break;
        }

        }
    }

    private void executeRequest(HttpUriRequest request, String url, Context context) throws IllegalStateException, IOException
    {

        prefs = new PrefStorage(context);
        int port = 80;
        if (prefs.hasValue(Constants.KEY_SERVER_PORT))
        {
            port = Integer.parseInt(prefs.getValue(Constants.KEY_SERVER_PORT));
        }
        HttpClient client = CustomSSLSocketFactory.getNewHttpClient(port);

        String proxyHost = "";
        String proxyPort = "";

        if (!(proxyHost.equals("") || proxyPort.equals("")))
        {
            HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        HttpResponse httpResponse;
        
        httpResponse = client.execute(request);

        responseCode = httpResponse.getStatusLine().getStatusCode();
        message = httpResponse.getStatusLine().getReasonPhrase();
        Header[] cookies_headers = httpResponse.getHeaders("Set-Cookie");

        for (int i = 0; i < cookies_headers.length; i++)
        {
            Header h = cookies_headers[i];
            cookies.setCookie(h.getName(), h.getValue());
            Log.d("cookie_client", h.getName() + " : " + h.getValue());
        }

        Log.d("network_json", "Rest Client response code " + responseCode);
        if (responseCode == 500 || responseCode == 404)
        {
            Log.d("errors", "Rest Client throwing HttpResponseException");
            throw new HttpResponseException(responseCode, "Error");
        }

        HttpEntity entity = httpResponse.getEntity();

        if (entity != null)
        {

            InputStream instream = entity.getContent();
            response = convertStreamToString(instream);

            // Closing the input stream will trigger connection release
            instream.close();
        }

    }

    private static String convertStreamToString(InputStream is)
    {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        } catch (IOException e)
        {
        } finally
        {
            try
            {
                is.close();
            } catch (IOException e)
            {
            }
        }
        return sb.toString();
    }
}
