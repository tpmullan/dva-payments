package com.praetorian.dva.fragments;

import java.util.Arrays;
import java.util.HashMap;

import com.praetorian.dva.R;
import com.praetorian.dva.activities.MainActivityGroup;
import com.praetorian.dva.db.ContactStorage;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Constants;
import com.praetorian.dva.rest.LoginRequest;
import com.praetorian.dva.rest.LoginResponse;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * LoginFragment, controls login GUI
 * 
 * @author richard
 * 
 */
public class LoginFragment extends Fragment implements OnClickListener, OnTouchListener
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.title_section1_signup;

    private PrefStorage prefs;
    private EditText usernameText;
    private EditText passwordText;
    private CheckBox rememberMe;
    private TextView signup;
    private TextView forgotpassword;
    private Button login;
    private ViewPager pager;

    private Context context;

    private View loginPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Log.println(Log.VERBOSE, "gui", "Creating Fragment for Login");

        loginPage = inflater.inflate(R.layout.fragment_main_signin, container, false);
        prefs = new PrefStorage(loginPage.getContext());
        context = loginPage.getContext();

        usernameText = (EditText) loginPage.findViewById(R.id.signin_usernamefield_edittext);
        usernameText.setOnClickListener(this);
        passwordText = (EditText) loginPage.findViewById(R.id.signin_passwordfield_edittext);
        passwordText.setOnClickListener(this);
        rememberMe = (CheckBox) loginPage.findViewById(R.id.signin_rememberme_checkbox);
        signup = (TextView) loginPage.findViewById(R.id.fragment_signin_signup_textview);
        forgotpassword = (TextView) loginPage.findViewById(R.id.fragment_signin_forgotpassword_textview);
        login = (Button) loginPage.findViewById(R.id.signin_submitbutton_button);

        login.setOnTouchListener(this);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);

        // Check if the user wanted their login information remembered
        if (prefs.getValue(Constants.KEY_REMEMBERME).equals(PrefStorage.TRUE))
        {
            usernameText.setText(prefs.getValue(Constants.KEY_USERNAME1));
            passwordText.setText(prefs.getValue(Constants.KEY_PASSWORD1));
            rememberMe.setChecked(true);
        }

        return loginPage;

    }

    /**
     * Adds the ViewPager object to the localclass. This allows this fragment to
     * control GUI elements of the activity from the fragment. For example, when
     * a user clicks the registration link, we can change the GUI to show the
     * registration fragment
     * 
     * @param pager
     */
    public void setPager(ViewPager pager)
    {
        this.pager = pager;

    }
    /** 
     * Shows a toast message to the user regarding errors or login issues
     * @param str (message)
     */
    private void showToastMessage(String str)
    {
        Context context = loginPage.getContext();
        Toast message = Toast.makeText(context, str, Toast.LENGTH_LONG);
        message.show();
    }
    /** 
     * Moves the application into the authenticated mode where the user can preform privilaged operations
     */
    private void showWelcome()
    {
        lockoutClear();
        Intent intent = new Intent(login.getContext(), MainActivityGroup.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
        case R.id.signin_submitbutton_button:
            startSubmitProcess();
            break;
        case R.id.fragment_signin_signup_textview:
            pager.setCurrentItem(1);
            break;
        case R.id.fragment_signin_forgotpassword_textview:
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + prefs.getValue(Constants.KEY_SERVER_HOST)
                    + "/password_resets/new"));
            startActivity(myIntent);
            break;

        }

    }

    @SuppressWarnings("unchecked")
    private void startSubmitProcess()
    {
        if (checkProxySettings() && passwordLockout())
        {
            checkSameUser();
            if (rememberMe.isChecked())
            {
                if (!prefs.hasValue(Constants.KEY_REMEMBERME))
                {
                    prefs.addValue(Constants.KEY_USERNAME1, usernameText.getText().toString());
                    prefs.addValue(Constants.KEY_PASSWORD1, passwordText.getText().toString());
                    prefs.addValue(Constants.KEY_REMEMBERME, PrefStorage.TRUE);
                } else
                {
                    prefs.setValue(Constants.KEY_USERNAME1, usernameText.getText().toString());
                    prefs.setValue(Constants.KEY_PASSWORD1, passwordText.getText().toString());
                    prefs.setValue(Constants.KEY_REMEMBERME, PrefStorage.TRUE);
                }
            } else
            {
                prefs.setValue(Constants.KEY_REMEMBERME, PrefStorage.FALSE);
            }
            HashMap<String, String> params = loginFormExtract();
            new LoginAsyncTask().execute(params);
        }
    }

    private void checkSameUser()
    {
        String oldUser = prefs.getValue(Constants.KEY_USERNAME1);
        if (!oldUser.equals(usernameText.getText().toString()))
        {
            Log.d("secure", "oldUser '" + oldUser + "'  ::  newUser '" + usernameText.getText().toString() + "'");
            prefs.destoryDB();
            ContactStorage cs = new ContactStorage(context);
            cs.destoryDB();
        }

    }

    private boolean passwordLockout()
    {
        long passwordTime = 0;
        if (prefs.hasValue(Constants.KEY_PASSWORD_LOCKOUT_TIME) && prefs.hasValue(Constants.KEY_PASSWORD_LOCKOUT))
        {
            passwordTime = Long.parseLong(prefs.getValue(Constants.KEY_PASSWORD_LOCKOUT_TIME));
            if (!prefs.getValue(Constants.KEY_PASSWORD_LOCKOUT).equals(PrefStorage.TRUE) && (passwordTime - System.currentTimeMillis() < 300000))
            {
                prefs.updateValue(Constants.KEY_PASSWORD_LOCKOUT_TIME, "" + (passwordTime + 60000));
                return true;
            } else
                if (passwordTime - System.currentTimeMillis() < 0)
                {
                    prefs.updateValue(Constants.KEY_PASSWORD_LOCKOUT, PrefStorage.FALSE);
                    return true;
                } else
                {
                    prefs.updateValue(Constants.KEY_PASSWORD_LOCKOUT, PrefStorage.TRUE);
                    showToastMessage("You have been locked out of your account. Please wait " + (passwordTime - System.currentTimeMillis()) / 60000
                            + " minutes");
                    return false;
                }
        }
        prefs.addValue(Constants.KEY_PASSWORD_LOCKOUT, PrefStorage.TRUE);
        prefs.addValue(Constants.KEY_PASSWORD_LOCKOUT_TIME, "" + (System.currentTimeMillis() + 60000));
        return true;
    }

    private void lockoutClear()
    {
        prefs.updateValue(Constants.KEY_PASSWORD_LOCKOUT_TIME, "" + System.currentTimeMillis());
    }

    private HashMap<String, String> loginFormExtract()
    {
        HashMap<String, String> refHash = new HashMap<String, String>();

        refHash.put(Constants.KEY_USERNAME1, usernameText.getText().toString());
        refHash.put(Constants.KEY_PASSWORD1, passwordText.getText().toString());

        return refHash;
    }

    private boolean checkProxySettings()
    {

        if (prefs.getValue(Constants.KEY_SERVER_HOST).equals(PrefStorage.NULL)
                || prefs.getValue(Constants.KEY_SERVER_PORT).equals(PrefStorage.NULL)
                || !((prefs.getValue(Constants.KEY_PROXY_HOST).equals(PrefStorage.NULL) && prefs.getValue(Constants.KEY_PROXY_PORT).equals(
                        PrefStorage.NULL)) || (!prefs.getValue(Constants.KEY_PROXY_HOST).equals(PrefStorage.NULL) && !prefs.getValue(
                        Constants.KEY_PROXY_PORT).equals(PrefStorage.NULL))))
        {
            showToastMessage("Unable to Login, Please fill out the server records");
            return false;
        }
        return true;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motion)
    {
        switch (view.getId())
        {
        case R.id.signin_usernamefield_edittext:
            usernameText.setBackgroundColor(Color.TRANSPARENT);
            return true;
        case R.id.signin_passwordfield_edittext:
            passwordText.setBackgroundColor(Color.TRANSPARENT);
            return true;
        }

        return false;
    }

    private class LoginAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> sendParamters = params[0];

            LoginRequest client = new LoginRequest(context);

            HashMap<String, String> userInfo = null;
            try
            {
                userInfo = client.authenticateUser(sendParamters.get(Constants.KEY_USERNAME1), sendParamters.get(Constants.KEY_PASSWORD1));

            } catch (Exception e)
            {
                userInfo = new HashMap<String, String>();
                userInfo.put("status", "false");
                userInfo.put("message", e.toString());
                Log.d("network_json", Arrays.toString(e.getStackTrace()));
            }

            return userInfo;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result)
        {
            if (result.get("status").equals("true"))
            {
                showToastMessage(result.get("message"));

                prefs.updateValue(LoginResponse.KEY_USERID, result.get("userId"));
                showWelcome();
            } else
            {
                showToastMessage("Unable to Login\n" + result.get("message"));
            }
        }
    }
}
