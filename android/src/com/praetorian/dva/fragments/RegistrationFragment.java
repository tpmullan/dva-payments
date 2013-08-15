package com.praetorian.dva.fragments;

import java.util.HashMap;

import com.praetorian.dva.R;
import com.praetorian.dva.activities.MainActivityGroup;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Constants;
import com.praetorian.dva.rest.LoginRequest;
import com.praetorian.dva.rest.LoginResponse;
import com.praetorian.dva.rest.RegisterRequest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationFragment extends Fragment implements OnClickListener
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.title_section2_register;

    private View page;
    private EditText name;
    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button register;
    private PrefStorage pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_main_register, container, false);
        pref = new PrefStorage(page.getContext());

        name = (EditText) page.findViewById(R.id.register_fullname_edittext);
        username = (EditText) page.findViewById(R.id.register_username_edittext);
        email = (EditText) page.findViewById(R.id.register_email_edittext);
        password1 = (EditText) page.findViewById(R.id.register_password1_edittext);
        password2 = (EditText) page.findViewById(R.id.register_password2_edittext);
        register = (Button) page.findViewById(R.id.register_registernow_button);

        register.setOnClickListener(this);

        restoreState();

        return page;
    }

    @Override
    public void onClick(View view)
    {
        saveState();
        submitData();
    }

    private void submitData()
    {
        if (passwordValidated())
        {
            String pw = password1.getText().toString();
            String pw2 = password2.getText().toString();
            String un = username.getText().toString();
            String fn = name.getText().toString();
            String em = email.getText().toString();

            HashMap<String, String> params = new HashMap<String, String>();

            params.put(Constants.KEY_USERNAME, un);
            params.put(Constants.KEY_NAME, fn);
            params.put(Constants.KEY_PASSWORD, pw);
            params.put(Constants.KEY_PASSWORD2, pw2);
            params.put(Constants.KEY_EMAIL, em);

            RegisterAsyncTask reg = new RegisterAsyncTask();
            reg.execute(params);
        } else
        {
            showToastMessage("Passwords do not Match");
        }

    }

    private boolean passwordValidated()
    {
        return password1.getText().toString().equals(password2.getText().toString());
    }

    private void saveState()
    {

        if (!((pref.getValue(Constants.KEY_PROXY_HOST).equals(PrefStorage.NULL) && pref.getValue(Constants.KEY_PROXY_PORT).equals(PrefStorage.NULL)) || (!pref
                .getValue(Constants.KEY_PROXY_HOST).equals(PrefStorage.NULL) && !pref.getValue(Constants.KEY_PROXY_PORT).equals(PrefStorage.NULL))))
        {
            showToastMessage("Unable to Login, Please ensure you have proxy/port or blank out the fields if not using a proxy");
            return;
        }

        pref.updateValue(Constants.KEY_USERNAME, username.getText().toString());
        pref.updateValue(Constants.KEY_NAME, name.getText().toString());
        pref.updateValue(Constants.KEY_PASSWORD, password1.getText().toString());
        pref.updateValue(Constants.KEY_PASSWORD2, password2.getText().toString());
        pref.updateValue(Constants.KEY_EMAIL, email.getText().toString());

    }

    private void restoreState()
    {
        username.setText(pref.getValue(Constants.KEY_USERNAME));
        name.setText(pref.getValue(Constants.KEY_NAME));
        password1.setText(pref.getValue(Constants.KEY_PASSWORD));
        email.setText(pref.getValue(Constants.KEY_EMAIL));
        password2.setText(pref.getValue(Constants.KEY_PASSWORD2));

    }

    private class RegisterAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> sendParamters = params[0];
            HashMap<String, String> success;

            RegisterRequest client = new RegisterRequest(page.getContext());

            HashMap<String, String> regSucess = null;
            try
            {
                success = client.registerUser(sendParamters.get(Constants.KEY_NAME), sendParamters.get(Constants.KEY_EMAIL),
                        sendParamters.get(Constants.KEY_USERNAME), sendParamters.get(Constants.KEY_PASSWORD),
                        sendParamters.get(Constants.KEY_PASSWORD2));

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

                pref.updateValue(Constants.KEY_PASSWORD, pref.getValue(Constants.KEY_PASSWORD));
                pref.updateValue(Constants.KEY_USERNAME, pref.getValue(Constants.KEY_EMAIL));
                pref.updateValue(Constants.KEY_REMEMBERME, pref.TRUE);
                pref.updateValue(LoginResponse.KEY_USERID, result.get(LoginResponse.KEY_USERID));
                loadWelcome();
                showToastMessage("Registration Succesful");
            } else
            {
                showToastMessage("Unable to Register\n" + result.get("message"));
            }
        }

    }

    private void loadWelcome()
    {
        Intent intent = new Intent(page.getContext(), MainActivityGroup.class);
        startActivity(intent);
    }

    private void showToastMessage(String str)
    {
        Context context = page.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();
    }
}
