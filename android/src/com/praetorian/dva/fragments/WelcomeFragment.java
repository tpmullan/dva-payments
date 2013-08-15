package com.praetorian.dva.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.praetorian.dva.R;
import com.praetorian.dva.activities.MainActivityGroup.SectionsPagerAdapter;
import com.praetorian.dva.db.AccountStorage;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Account;
import com.praetorian.dva.misc.Constants;
import com.praetorian.dva.misc.Transaction;
import com.praetorian.dva.rest.LoginResponse;
import com.praetorian.dva.rest.TransactionRequest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeFragment extends Fragment
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.welcome_name_title;
    private ViewPager pager;
    private View page;
    private ViewGroup container;
    private ListView listview;
    private TextView username;
    private AccountStorage accountStorage;

    private PrefStorage prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_auth_welcome, container, false);

        accountStorage = new AccountStorage(page.getContext());

        listview = (ListView) page.findViewById(R.id.welcome_listview);
        username = (TextView) page.findViewById(R.id.welcome_username_textview);

        this.container = container;

        prefs = new PrefStorage(page.getContext());

        MyMoneyGetAccountsAsyncTask accountGraber = new MyMoneyGetAccountsAsyncTask();
        accountGraber.execute();

        username.setText(prefs.getValue(Constants.KEY_USERNAME1));

        return page;
    }

    public void setPager(ViewPager pager)
    {
        this.pager = pager;

    }


    private class TransactionsArrayAdapter extends ArrayAdapter<Account> implements OnClickListener
    {
        private Context context;
        private List<Account> values;

        public TransactionsArrayAdapter(Context context, ArrayList<Account> values)
        {
            super(context, R.layout.listview_account_summery, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview_account_summery, parent, false);
            rowView.setOnClickListener(this);
            TextView AccountName = (TextView) rowView.findViewById(R.id.account_summery_accountname);
            TextView AccountNumber = (TextView) rowView.findViewById(R.id.account_summery_accountnumber);
            TextView Balance = (TextView) rowView.findViewById(R.id.account_summery_balance);

            AccountName.setText(values.get(position).getAccountName());
            AccountNumber.setText(accountFormat(values.get(position).getAccountNumber()));
            Balance.setText("$" + values.get(position).getAccountBalance());

            return rowView;

        }

        private CharSequence accountFormat(String accountNumber)
        {
            return "(" + accountNumber.substring(accountNumber.length() - 4) + ")";
        }

        @Override
        public void onClick(View v)
        {
            pager.setCurrentItem(1);
            
        }

    }

    private class MyMoneyGetAccountsAsyncTask extends AsyncTask<Void, Void, HashMap<String, Object>>
    {

        @Override
        protected HashMap<String, Object> doInBackground(Void... voids)
        {
            HashMap<String, Object> success = null;
            TransactionRequest client = new TransactionRequest(page.getContext());

            {
                try
                {
                    success = client.getAccounts();

                } catch (Exception e)
                {
                    success = new HashMap<String, Object>();
                    success.put("status", "false");
                    success.put("message", e.toString());
                }
            }
            return success;

        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result)
        {
            if (((String) result.get("status")).equals("true"))
            {
                ArrayList<Account> accounts = (ArrayList<Account>) result.get("accounts");

                updateAccountDatabase(accounts);

                TransactionsArrayAdapter adapter = new TransactionsArrayAdapter(page.getContext(), accounts);

                listview.setAdapter(adapter);

            }
        }

        private void updateAccountDatabase(ArrayList<Account> accounts)
        {
            for(int k = 0; k < accounts.size(); k++)
            {
                accountStorage.addAccount(accounts.get(k));
            }
        }

    }

    private void showToastMessage(String str)
    {
        Context context = page.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();
    }

}
