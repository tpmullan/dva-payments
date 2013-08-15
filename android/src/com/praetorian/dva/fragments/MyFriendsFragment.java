package com.praetorian.dva.fragments;

import java.util.ArrayList;
import java.util.List;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.praetorian.dva.R;
import com.praetorian.dva.db.Contact;
import com.praetorian.dva.db.ContactStorage;
import com.praetorian.dva.service.ContactPopulaterService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyFriendsFragment extends Fragment
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.myfriends_name_title;
    private View page;
    private Context context;
    private PullToRefreshListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_auth_myfriends, container, false);
        context = page.getContext();
        listview = (PullToRefreshListView) page.findViewById(R.id.myfrends_listview);

        listview.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                startLoadup();
            }

        });

        loadListView();

        return page;
    }

    private void loadListView()
    {
        ContactStorage cs = new ContactStorage(context);
        
        
        
        
        
        ContactArrayAdapter adapter = new ContactArrayAdapter(page.getContext(), cs.getContacts());
        listview.setAdapter(adapter);

    }

    private class ContactArrayAdapter extends ArrayAdapter<Contact>
    {
        private Context context;
        private List<Contact> values;

        public ContactArrayAdapter(Context context, ArrayList<Contact> values)
        {
            super(context, R.layout.contactlistview, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.contactlistview, parent, false);

            TextView name = (TextView) rowView.findViewById(R.id.contacts_name_textview);
            TextView email = (TextView) rowView.findViewById(R.id.contacts_email_textview);

            name.setText(values.get(position).getName());
            email.setText(values.get(position).getEmail());

            return rowView;
        }

    }

    private void startLoadup()
    {
        Intent intent = new Intent(context, ContactPopulaterService.class);
        Messenger messenger = new Messenger(handler);
        intent.putExtra("MESSENGER", messenger);
        getActivity().startService(intent);

        // progress.setEnabled(true);
        Log.v("serviceLog", "Button has been clicked");

    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {

            String output = (String) message.obj;
            Toast.makeText(context, output, Toast.LENGTH_SHORT).show();
            listview.onRefreshComplete();
            loadListView();
            

        };
    };

}
