package com.praetorian.dva.activities;

/**
 * Damn Vulnerable Application
 * 
 * @author Richard Penshorn
 * Praetorian Labs 2013
 */
import java.util.Arrays;
import java.util.HashMap;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegratorSupportV4;
import com.google.zxing.integration.android.IntentResult;
import com.praetorian.dva.R;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.misc.Constants;
import com.praetorian.dva.rest.TransferRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SendingMoney extends Activity implements OnClickListener, CreateNdefMessageCallback, OnNdefPushCompleteCallback
{
    private static final int MESSAGE_SENT = 0;
    private Context context;
    private String account;
    private String amount;
    private String mode;
    private String token;
    private Button qr;
    private NfcAdapter mNfcAdapter;
    private TextView nfcInfo;

    PrefStorage prefs;

    public final static String NFC_POST_KEY = "TOACCOUNTnfc";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this.getBaseContext();
        setContentView(R.layout.activity_qrnfc);
        prefs = new PrefStorage(context);

        qr = (Button) findViewById(R.id.qrnfc_qrget_button);
        nfcInfo = (TextView) findViewById(R.id.qrnfc_nfcinfo_textview);

        Bundle b = getIntent().getExtras();
        mode = b.getString("mode");
        account = b.getString("account");
        amount = b.getString("amount");

        if(account != null)
        {
            prefs.updateValue(NFC_POST_KEY, account);
            Log.d("nfc","updating database with account token for nfc");
        }
            
        

        qr.setOnClickListener(this);
        
        

        if (mode != null && !mode.equals("send"))
        {
            nfcInfo.setText("NFC Ready!");
        }
        else if (mode != null)
        {
            startTransferToken(amount, account, false);
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null)
        {
            nfcInfo = (TextView) findViewById(R.id.qrnfc_nfcinfo_textview);
            nfcInfo.setText("NFC is not available on this device.");
        } else
        {
            // Register callback to set NDEF message
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            // Register callback to listen for message-sent success
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }

    }

    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        Log.d("qr", "Scan handler called");
        if (scanResult != null)
        {
            Log.d("qr", "Scan Result was: " + scanResult.getContents());
            TransferResponseAsyncTask respondTask = new TransferResponseAsyncTask();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Constants.KEY_TOKEN, scanResult.getContents());
            params.put(Constants.KEY_AMOUNTMOVE, account);
            respondTask.execute(params);
        }

    }

    @SuppressWarnings("unchecked")
    private void startTransferToken(String amountToMove, String fromAccountToMove, boolean qr_mode)
    {

        if (token != null)
        {
            createShowQR(token);
            return;
        }
        TransferAsyncTask task = new TransferAsyncTask();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put(Constants.KEY_FROMACCOUNT, fromAccountToMove);
        params.put(Constants.KEY_AMOUNTMOVE, amountToMove);
        params.put("mode", Boolean.toString(qr_mode));

        task.execute(params);
    }

    private class TransferResponseAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> result = null;
            HashMap<String, String> sendParamters = params[0];
            TransferRequest client = new TransferRequest(context);

            try
            {
                result = client.sendToken(sendParamters.get(Constants.KEY_TOKEN), sendParamters.get(Constants.KEY_AMOUNTMOVE));

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
                finish();
            }
            showToastMessage(result.get("message"));
        }

    }

    /**
     * Shows a simple Popup message to the user regarding any GUI Elements that
     * might of changed.
     * 
     * @param str
     *            (Input String to display)
     */
    private void showToastMessage(String str)
    {
        Toast message = Toast.makeText(context, str, Toast.LENGTH_LONG);
        message.show();

    }

    /**
     * Creates an intent using the zxing library to generate the QR Code
     * required for making a token transfer between two phones. Takes in a
     * string paramter and will create a QR code off the string.
     * 
     * @param token
     */
    public void createShowQR(String token)
    {
        IntentIntegrator sendText = new IntentIntegrator(this);
        sendText.shareText(token);

    }

    /**
     * Async Class for retrieving token from the backend server. Used to
     * reterive data fro mthe backend server regarding token transfers.
     * 
     * Updates GUI Elements when token is ready for display
     * 
     * @author Richard Penshorn
     * 
     */
    private class TransferAsyncTask extends AsyncTask<HashMap<String, String>, Void, HashMap<String, String>>
    {
        @Override
        protected HashMap<String, String> doInBackground(HashMap<String, String>... params)
        {
            HashMap<String, String> result = null;
            HashMap<String, String> sendParamters = params[0];
            TransferRequest client = new TransferRequest(context);

            try
            {
                result = client.getToken(sendParamters.get(Constants.KEY_FROMACCOUNT), sendParamters.get(Constants.KEY_AMOUNTMOVE));

            } catch (Exception e)
            {
                result = new HashMap<String, String>();
                result.put("status", "false");
                result.put("message", e.toString());
                Log.d("network_json", Arrays.toString(e.getStackTrace()));
            }

            result.put("mode", sendParamters.get("mode"));

            return result;

        }

        @Override
        protected void onPostExecute(HashMap<String, String> result)
        {
            if (result.get("status").equals("true"))
            {
                if (result.get("mode").equals("qr"))
                {
                    createShowQR(result.get("token"));
                } else
                {
                    token = result.get("token");
                    nfcInfo.setText("Ready to Send via NFC");
                }
            }
            showToastMessage(result.get("message") + result.get("token"));
        }
    }

    @Override
    public void onClick(View arg0)
    {

        switch (arg0.getId())
        {
        case R.id.qrnfc_qrget_button:

            if (mode.equals("send"))
            {
                String amountToMove = amount;
                String fromAccountToMove = account;
                startTransferToken(amountToMove, fromAccountToMove, false);
                // String token =
                // getTransferToken(amountToMove,fromAccountToMove);
                // sendText.shareText(amountToMove + ":" + token);
            } else
            {

                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
            }

        }

    }

    /**
     * Function to handle when the Activity is called outside of the
     * application. This is useful so the application does not have to be open
     * for a NFC transfer to happen. In that case that the NFC transfer happens,
     * it will pull the last account which money was transfered to.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()))
        {

            processIntent(getIntent());
        }
    }

    /**
     * is Called when a new intent is created outside of the application. In the
     * android manifest we have setup an intent filter to call this Activity
     * during a NFC Transfer.
     */
    @Override
    public void onNewIntent(Intent intent)
    {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Will take information from the NFC Library and start the task of
     * responding to the backend server with the token. This will allow the user
     * to make a NFC transfer using android beam
     * 
     * @param intent
     *            (current intent)
     */
    @SuppressWarnings("unchecked")
    private void processIntent(Intent intent)
    {
        Log.d("nfcdebug", "Processing Intent");
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String token = (new String(msg.getRecords()[0].getPayload()));
        TransferResponseAsyncTask respondTask = new TransferResponseAsyncTask();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constants.KEY_TOKEN, token);
        params.put(Constants.KEY_AMOUNTMOVE, prefs.getValue(NFC_POST_KEY));
        Log.d("nfc_callback","Got request and my account is: " + prefs.getValue(NFC_POST_KEY));
        respondTask.execute(params);

    }

    @Override
    public void onNdefPushComplete(NfcEvent arg0)
    {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }

    /** This handler receives a message from onNdefPushComplete */
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case MESSAGE_SENT:
                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();
                break;
            }
        }
    };

    /**
     * Creates a NDEF style message. During a sending event, the NDEF message is
     * created for use during the Android Beam event. 
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent arg0)
    {
        if (mode.equals("send"))
        {

            NdefMessage msg = new NdefMessage(NdefRecord.createMime("application/com.praetorian.dva.activities.sendingmoney", token.getBytes()));
            // NdefRecord.createApplicationRecord("com.praetorian.dva.activities.sendingmoney");
            return msg;
            
        }
        NdefMessage msg = new NdefMessage(NdefRecord.createMime("application/com.praetorian.dva.activities.sendingmoney", "nothing".getBytes()));
        return msg;
    }
}
