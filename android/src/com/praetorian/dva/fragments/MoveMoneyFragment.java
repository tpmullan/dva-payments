package com.praetorian.dva.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegratorSupportV4;
import com.google.zxing.integration.android.IntentResult;
import com.praetorian.dva.R;
import com.praetorian.dva.activities.SendingMoney;
import com.praetorian.dva.db.AccountStorage;
import com.praetorian.dva.db.Contact;
import com.praetorian.dva.db.ContactStorage;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Account;
import com.praetorian.dva.misc.Constants;
import com.praetorian.dva.rest.TransferRequest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MoveMoneyFragment extends Fragment implements OnClickListener, OnLongClickListener, OnSeekBarChangeListener
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.movemoney_name_title;
    private View page;
    private Context context;
    private PrefStorage prefs;
    private Button moveMoney;
    private EditText toAccount;
    private Spinner fromAccount;
    private EditText amount;
    private String[] toAccounts;
    private SeekBar bar;
    private TextView status;
    private ArrayList<Account> listAccount;
    ContactStorage cs;
    private int mode = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_auth_movemoney, container, false);
        context = page.getContext();
        prefs = new PrefStorage(context);

        moveMoney = (Button) page.findViewById(R.id.movemoney_movemymoney_button);

        toAccount = (EditText) page.findViewById(R.id.movemovey_toAccount_edittext);
        fromAccount = (Spinner) page.findViewById(R.id.movemoney_fromaccount_edittext);
        amount = (EditText) page.findViewById(R.id.movemoney_amount_edittext);
        bar = (SeekBar) page.findViewById(R.id.threeStageSeekBar);
        status = (TextView) page.findViewById(R.id.movemoney_mode_textview);

        bar.setMax(2);
        bar.setOnSeekBarChangeListener(this);

        status.setText("Simple Transfer Mode");

        cs = new ContactStorage(page.getContext());
        listAccount = new ArrayList<Account>();
        
        populateFromAccount();

        toAccount.setOnLongClickListener(this);
        fromAccount.setOnLongClickListener(this);

        moveMoney.setOnClickListener(this);

        return page;
    }

    private void populateFromAccount()
    {
        AccountStorage as = new AccountStorage(page.getContext());
        ArrayList<Account> accounts = as.getAccounts();
        ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(context, android.R.layout.simple_list_item_1, accounts);
        fromAccount.setAdapter(adapter);
    }



    public void clearFields()
    {
        toAccount.setText("");
        amount.setText("");
    }

    private void updateUser()
    {
        clearFields();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
        case R.id.movemoney_movemymoney_button:
        {
            if (mode == 0)
            {
                HashMap<String, String> params = new HashMap<String, String>();
                SimpleTransactionAsyncTask moveMoney = new SimpleTransactionAsyncTask();
                params.put(TransferRequest.POST_AMOUNT, amount.getText().toString());
                params.put(TransferRequest.POST_FROMACCOUNT, ((Account) fromAccount.getSelectedItem()).getAccountNumber());
                params.put(TransferRequest.POST_TOACCOUNT, toAccount.getText().toString());
                moveMoney.execute(params);
            } else
            {
                Intent intent = new Intent(this.getActivity(), SendingMoney.class);
                if (!checkFieldsValid())
                {
                    showToastMessage("You need to fill out the form. \nSending Money - Amount and From Account\nGetting Money - To Account");
                    return;
                }
                Bundle b = getBundleMode();

                intent.putExtras(b);
                startActivity(intent);
            }
        }
            break;

        }

    }

    private boolean checkFieldsValid()
    {
        if (toAccount.getText().toString().length() > 0)
        {
            return true;
        } else
            if (amount.getText().toString().length() > 0 && (toAccount.getText().toString().length() == 0))
            {
                return true;
            }
        return false;
    }

    private Bundle getBundleMode()
    {
        // if the user want to send money (from account and amount need to be
        // filed out)
        Bundle b = new Bundle();
        if (mode == 1)
        {
            b.putString("mode", "get");
            b.putString("account", toAccount.getText().toString());
        } else
            if (mode == 2)
            {
                b.putString("mode", "send");
                b.putString("account", ((Account) fromAccount.getSelectedItem()).getAccountNumber());
                b.putString("amount", amount.getText().toString());
            }

        return b;
    }

    private class SimpleTransactionAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> result = null;
            HashMap<String, String> sendParamters = params[0];
            TransferRequest client = new TransferRequest(context);

            try
            {
                result = client.sendTransfer(sendParamters.get(TransferRequest.POST_TOACCOUNT), sendParamters.get(TransferRequest.POST_FROMACCOUNT),
                        Double.parseDouble(sendParamters.get(TransferRequest.POST_AMOUNT)));
            } catch (Exception e)
            {
                result = new HashMap<String, String>();
                result.put("status", "false");
                result.put("message", e.toString());
                Log.d("network_json", Arrays.toString(e.getStackTrace()));
            }

            return result;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result)
        {
            if (result.get("status").equals("true"))
            {
                clearFields();
            }
            showToastMessage(result.get("message"));
        }
    }

    private void showToastMessage(String str)
    {
        Context context = page.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();

    }

    private void showDialogtoAccount()
    {
        
        listAccount.clear();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Select To Account");
        
        //Get Users Local Accounts
        AccountStorage as = new AccountStorage(page.getContext());
        ArrayList<Account> accounts = as.getAccounts();
        
        listAccount.addAll(accounts);
        //Get Users Friends Accounts
        ArrayList<Contact> friendsContacts = cs.getContacts();
        
        //translate the contacts to accounts
        for(int k = 0; k < friendsContacts.size(); k++)
        {
           Contact f = friendsContacts.get(k);
           listAccount.add(new Account(f.getEmail(),f.getName(),0,true));
        }
        
        String[] outputListAccount = new String[listAccount.size()];
        
        for(int k = 0; k < outputListAccount.length; k++)
        {
            outputListAccount[k] = listAccount.get(k).toString();
        }
       
        builder.setItems(outputListAccount, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                toAccount.setText(listAccount.get(which).getAccountNumber());

            }
        });

        builder.create().show();
    }

    @Override
    public boolean onLongClick(View view)
    {
        switch (view.getId())
        {
        case R.id.movemovey_toAccount_edittext:
        {
            showDialogtoAccount();
            break;
        }
        }

        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seek, int progress, boolean fromUser)
    {
        switch (progress)
        {
        case 0:
            toAccount.setEnabled(true);
            fromAccount.setEnabled(true);
            amount.setEnabled(true);
            status.setText("Simple Transfer Mode");
            mode = 0;
            break;
        case 1:
            toAccount.setEnabled(true);
            fromAccount.setEnabled(false);
            amount.setEnabled(false);
            amount.setText("");
            status.setText("Receive Mode");
            mode = 1;
            break;
        case 2:
            toAccount.setEnabled(false);
            toAccount.setText("");
            fromAccount.setEnabled(true);
            amount.setEnabled(true);
            status.setText("Send Mode");
            mode = 2;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0)
    {
        // TODO Auto-generated method stub

    }
}
