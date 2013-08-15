package com.praetorian.dva.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.praetorian.dva.R;
import com.praetorian.dva.db.AccountStorage;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Account;
import com.praetorian.dva.misc.Transaction;
import com.praetorian.dva.rest.LoginResponse;
import com.praetorian.dva.rest.MyAccountRequest;
import com.praetorian.dva.rest.TransactionRequest;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MyMoneyFragment extends Fragment
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.mymoney_name_title;
    private ArrayList<View> accounts;
    private View page;
    private ViewGroup pager;
    private ViewGroup container;
    private ArrayList<Account> accountList;

    private ArrayList<Transaction> values;
    private AccountStorage accountStorage;
    private LayoutInflater inflater;
    private PrefStorage prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_auth_mymoney, container, false);
        this.inflater = inflater;
        this.container = container;
        prefs = new PrefStorage(page.getContext());

        accountStorage = new AccountStorage(page.getContext());
        accounts = new ArrayList<View>();

        UpdateAccountInformation update = new UpdateAccountInformation();
        update.execute();

        return page;
    }

    private class UpdateAccountInformation extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... arg0)
        {
            // first we need to get a number of accounts. This was given to us from
            // the main page, but there may be updates
            // update account information
            accountStorage.updateAccounts();
            Log.v("mymoney", "Updating Account Information");
            return null;

        }

        protected void onPostExecute(Void a)
        {
            populateAccountUI();
        }

    }

    private void populateAccountUI()
    {
        Log.v("mymoney", "Creating Account Views");


        // next we need to get accounts and create the UI elements for each one
        // on the page.
        accountList = accountStorage.getAccounts();
        LinearLayout container = (LinearLayout) page.findViewById(R.id.mymoney_container);
        Log.v("mymoney", "Creating Account Views. Number of accounts to create: " + accountList.size());
        for (int k = 0; k < accountList.size(); k++)
        {
            Account account = accountList.get(k);

            View accountView = inflater.inflate(R.layout.account_layout, null);

            // create UI Elements
            TextView accountName = (TextView) accountView.findViewById(R.id.mymoney_accountnumber_textview);
            TextView balance = (TextView) accountView.findViewById(R.id.mymoney_balance_textview);

            // Populate those elements with the correct information
            accountName.setText(account.getFriendlyName());
            balance.setText("" + account.getAccountBalance());

            // setup the pulldown menu for each of the accounts
            final PullToRefreshListView pulldown = (PullToRefreshListView) accountView.findViewById(R.id.listView1);

            // create on click listener to hide the accounts.
            RelativeLayout topBar = (RelativeLayout) accountView.findViewById(R.id.mymoney_topbar);
            topBar.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    if (pulldown.getVisibility() == PullToRefreshListView.VISIBLE)
                    {
                        pulldown.setVisibility(PullToRefreshListView.GONE);
                    } else
                    {
                        pulldown.setVisibility(PullToRefreshListView.VISIBLE);
                    }

                }

            });
            // create task to populate and update the transactions for each
            // account
            
            MyMoneyGetAsyncTask task = new MyMoneyGetAsyncTask();
            task.execute(k,account.getId());
            
            final int ui_id = k;
            final int account_id = account.getId();
            
            //setup pulldown refresh handlers
            pulldown.setOnRefreshListener(new OnRefreshListener()
            {
                
                @Override
                public void onRefresh()
                {
                    MyMoneyGetAsyncTask task = new MyMoneyGetAsyncTask();
                    task.execute(ui_id,account_id);
                    
                }
                
            });
           

            // add view to the root page
            Log.v("mymoney", "adding new account to the pageview id:" + k);

            container.addView(accountView, k);
            accounts.add(accountView);
        }

    }

    public class MyMoneyGetAsyncTask extends AsyncTask<Integer, Void, HashMap<String, Object>>
    {
        private int UI_ID = 0;
        private int Account_ID = 0;
        @Override
        protected HashMap<String, Object> doInBackground(Integer... var)
        {
            
            HashMap<String, Object> success = null;
            TransactionRequest client = new TransactionRequest(page.getContext());
            String userId = prefs.getValue(LoginResponse.KEY_USERID);
            UI_ID = var[0];
            Account_ID = var[1];
            {
                try
                {
                    success = client.getTransactions(userId,Account_ID);

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
                values = (ArrayList<Transaction>) result.get("transactions");
                TransactionsArrayAdapter adapter = new TransactionsArrayAdapter(page.getContext(), values);

                View accountView = accounts.get(UI_ID);
                
                PullToRefreshListView listview = (PullToRefreshListView) accountView.findViewById(R.id.listView1);
                
                
                listview.setAdapter(adapter);
                listview.onRefreshComplete();
            } else
            {
                showToastMessage("Unable to fetch \n" + ((String) result.get("message")).toString());
                Log.d("network_json", "error" + ((String) result.get("message")).toString());
            }
        }
    }

    public void setPager(ViewPager pager)
    {
        this.pager = pager;

    }

    private class TransactionsArrayAdapter extends ArrayAdapter<Transaction>
    {
        private Context context;
        private List<Transaction> values;

        public TransactionsArrayAdapter(Context context, ArrayList<Transaction> values)
        {
            super(context, R.layout.transactionlayoutheader, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.transactionlayoutheader, parent, false);
            TextView date = (TextView) rowView.findViewById(R.id.transaction_date);
            TextView amount = (TextView) rowView.findViewById(R.id.transactions_amount);
            TextView toAccount = (TextView) rowView.findViewById(R.id.transaction_toaccount);
            TextView fromAccount = (TextView) rowView.findViewById(R.id.transaction_fromaccount);
            TextView memo = (TextView) rowView.findViewById(R.id.transaction_memo);
            TextView balance = (TextView) rowView.findViewById(R.id.transaction_balance);

            date.setText(values.get(position).getTime().toString());
            amount.setText("" + values.get(position).getAmount());
            toAccount.setText(values.get(position).getToAccount());
            fromAccount.setText(values.get(position).getFromAccount());
            memo.setText(values.get(position).getMemo());
            balance.setText("" + values.get(position).getBalance());

            return rowView;

        }

    }

    private void showToastMessage(String str)
    {
        Context context = page.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();

    }

}
