package com.praetorian.dva.fragments;

import com.praetorian.dva.R;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Constants;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProxyFragment extends Fragment implements OnClickListener
{
    public static final int NAME_FRAGMENT_RESOURCE = R.string.title_section3_proxy;

    private View page;

   //private EditText proxy_host;
    //private EditText proxy_port;
    private EditText server_host;
    private EditText server_port;
    private Button updateConfig;

    private PrefStorage pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        page = inflater.inflate(R.layout.fragment_main_proxy, container, false);
        pref = new PrefStorage(page.getContext());

      //  proxy_host = (EditText) page.findViewById(R.id.proxy_host_edittext);
       // proxy_port = (EditText) page.findViewById(R.id.proxy_port_edittext);
        server_host = (EditText) page.findViewById(R.id.proxy_server_host_edittext);
        server_port = (EditText) page.findViewById(R.id.proxy_server_port_edittext);
        updateConfig = (Button) page.findViewById(R.id.proxy_updateconfig_button);

        updateConfig.setOnClickListener(this);

        restoreState();

        return page;

    }

    private void showToastMessage(String str)
    {
        Context context = page.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();
    }

    private void restoreState()
    {
        if (pref.hasValue(Constants.KEY_PROXY_HOST))
        {
          //  proxy_host.setText(pref.getValue(Constants.KEY_PROXY_HOST));
        }
        if (pref.hasValue(Constants.KEY_PROXY_PORT))
        {
          //  proxy_port.setText(pref.getValue(Constants.KEY_PROXY_PORT));
        }
        if (pref.hasValue(Constants.KEY_SERVER_HOST))
        {
            server_host.setText(pref.getValue(Constants.KEY_SERVER_HOST));
        }
        if (pref.hasValue(Constants.KEY_SERVER_PORT))
        {
            server_port.setText(pref.getValue(Constants.KEY_SERVER_PORT));
        }
    }

    private void saveState()
    {
     //   pref.updateValue(Constants.KEY_PROXY_HOST, proxy_host.getText().toString());
     //   pref.updateValue(Constants.KEY_PROXY_PORT, proxy_port.getText().toString());
        pref.updateValue(Constants.KEY_SERVER_HOST, server_host.getText().toString());
        pref.updateValue(Constants.KEY_SERVER_PORT, server_port.getText().toString());
    }

    @Override
    public void onClick(View view)
    {
        saveState();
        showToastMessage("Updated Config");

    }

}
