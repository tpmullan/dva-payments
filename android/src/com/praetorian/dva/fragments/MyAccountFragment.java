package com.praetorian.dva.fragments;

import java.util.HashMap;

import com.praetorian.dva.R;
import com.praetorian.dva.activities.LoginActivityGroup;
import com.praetorian.dva.activities.MainActivityGroup;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Constants;
import com.praetorian.dva.popup.PasswordChangerPopup;
import com.praetorian.dva.rest.LoginResponse;
import com.praetorian.dva.rest.MyAccountRequest;
import com.praetorian.dva.rest.RegisterRequest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyAccountFragment extends Fragment implements OnTouchListener, OnClickListener
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.myaccount_name_title;

    private View page;
    private EditText username;
    private EditText password;
    private EditText email;
    private Button deleteAccount;
    private PrefStorage prefs;
    private Button updateAccount;

    private PasswordChangerPopup popup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_auth_myaccount, container, false);
        prefs = new PrefStorage(page.getContext());

        username = (EditText) page.findViewById(R.id.myaccount_username_edittext);
        password = (EditText) page.findViewById(R.id.myaccount_password_edittext);
        email = (EditText) page.findViewById(R.id.myaccount_email_edittext);
        deleteAccount = (Button) page.findViewById(R.id.myaccount_delete_button);
        updateAccount = (Button) page.findViewById(R.id.myaccount_updateAccount_button);
        deleteAccount.setOnClickListener(this);

        popup = new PasswordChangerPopup(inflater.inflate(R.layout.popup_password_myaccount, null), page.getContext());

        password.setOnTouchListener(this);
        updateAccount.setOnClickListener(this);

        MyAccountGetAsyncTask updateFields = new MyAccountGetAsyncTask();
        updateFields.execute();

        return page;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
        case R.id.myaccount_delete_button:
            deleteAccount();
            break;
        case R.id.myaccount_updateAccount_button:
            updateAccount();
        }

    }

    private void deleteAccount()
    {
        MyAccountDeleteAsyncTask task = new MyAccountDeleteAsyncTask();
        HashMap<String, String> params = new HashMap<String, String>();
        task.execute(params);
    }

    private void updateAccount()
    {
        HashMap<String, String> updateValues = new HashMap<String, String>();
        if (!prefs.getValue(Constants.KEY_USERNAME_OLD).equals(username.getText().toString()))
        {
            updateValues.put(MyAccountRequest.POST_USERNAME, username.getText().toString());
        }
        if (!prefs.getValue(Constants.KEY_EMAIL_OLD).equals(email.getText().toString()))
        {
            updateValues.put(MyAccountRequest.POST_EMAIL, email.getText().toString());
        }
        if (!prefs.getValue(Constants.KEY_PASSWORD_OLD).equals("nothing"))
        {
            updateValues.put(MyAccountRequest.POST_PASSWORD, prefs.getValue(Constants.KEY_PASSWORD_OLD));
            updateValues.put(MyAccountRequest.POST_PASSWORD2, prefs.getValue(Constants.KEY_PASSWORD_OLD));
        }
        MyAccountUpdateAsyncTask task2 = new MyAccountUpdateAsyncTask();
        task2.execute(updateValues);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motion)
    {
        switch (view.getId())
        {
        case R.id.myaccount_password_edittext:
            // Only want when an user taps on the field
            if (motion.getAction() == MotionEvent.ACTION_DOWN)
            {
                popup.show(username, -150, -80);
                username.setFreezesText(true);
                email.setFreezesText(true);
            }
            break;
        }

        return true;
    }

    private void showToastMessage(String str)
    {
        Context context = page.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();

    }

    private void logoffUser()
    {
        Intent intent = new Intent(page.getContext(), LoginActivityGroup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void populateGUI(String email, String password, String username)
    {
        this.username.setText(username);
        this.password.setText(password);
        this.email.setText(email);
        prefs.updateValue(Constants.KEY_EMAIL_OLD, email);
        prefs.updateValue(Constants.KEY_PASSWORD_OLD, password);
        prefs.updateValue(Constants.KEY_USERNAME_OLD, username);
    }

    private class MyAccountUpdateAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> param = params[0];
            HashMap<String, String> success = null;

            MyAccountRequest client = new MyAccountRequest(page.getContext());
            String userId = prefs.getValue(LoginResponse.KEY_USERID);

            try
            {

                success = client.updateUser(param, userId);

            } catch (Exception e)
            {
                success = new HashMap<String, String>();
                success.put("status", "false");
                success.put("message", e.toString());
            }

            return success;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result)
        {
            if (result.get("status").equals("true"))
            {
                logoffUser();
            } else
            {
                showToastMessage("Unable to Comply\n" + result.get("message"));
            }
        }
    }

    private class MyAccountDeleteAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> param = params[0];
            HashMap<String, String> success = null;
            MyAccountRequest client = new MyAccountRequest(page.getContext());
            String userId = prefs.getValue(LoginResponse.KEY_USERID);
            try
            {

                success = client.deleteUser(userId);

            } catch (Exception e)
            {
                success = new HashMap<String, String>();
                success.put("status", "false");
                success.put("message", e.toString());
            }

            return success;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result)
        {
            if (result.get("status").equals("true"))
            {

                logoffUser();

            } else
            {
                showToastMessage("Unable to Comply\n" + result.get("message"));
            }
        }
    }

    private class MyAccountGetAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(Void... voids)
        {

            HashMap<String, String> success = null;
            MyAccountRequest client = new MyAccountRequest(page.getContext());
            String userId = prefs.getValue(LoginResponse.KEY_USERID);
            try
            {

                success = client.getUser(userId);

            } catch (Exception e)
            {
                success = new HashMap<String, String>();
                success.put("status", "false");
                success.put("message", e.toString());
            }
            return success;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result)
        {
            if (result.get("status").equals("true"))
            {
                populateGUI(result.get(MyAccountRequest.POST_EMAIL), "nothing", result.get(MyAccountRequest.POST_USERNAME));
            } else
            {
                showToastMessage("Unable to Comply\n" + result.get("message"));
            }
        }
    }
}
