package com.praetorian.dva.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.client.HttpResponseException;

import com.praetorian.dva.db.Contact;
import com.praetorian.dva.db.ContactStorage;
import com.praetorian.dva.rest.ContactCheckRequeset;
import com.praetorian.dva.rest.LoginResponse;
import com.praetorian.dva.rest.MyAccountRequest;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactPopulaterService extends IntentService
{
    private ContactStorage contactDB;

    public ContactPopulaterService()
    {
        super("ContactPopulater");
        Log.v("serviceLog", "Constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Uri data = intent.getData();
        String urlPath = intent.getStringExtra("urlpath");

        contactDB = new ContactStorage(this.getBaseContext());

        // code to grab their contact book and dump to the server.

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        ArrayList<Contact> contactsForVerify = new ArrayList<Contact>();

        while (cursor.moveToNext())
        {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

            Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            String emailAddress = null;
            while (emails.moveToNext())
            {
                // This would allow you get several email addresses
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emails.close();

            if (name != null && emailAddress != null && !name.isEmpty() && !emailAddress.isEmpty())
                contactsForVerify.add(new Contact(name, "", emailAddress));

        }
        cursor.close();
        // clear db
        contactDB.cleanContact();

        // network code;
        String[] emails = new String[contactsForVerify.size()];
        for (int k = 0; k < contactsForVerify.size(); k++)
        {
            Contact c = contactsForVerify.get(k);
            emails[k] = c.getEmail();
        }
        // check network

        ContactCheckRequeset client = new ContactCheckRequeset(getBaseContext());
        Boolean succeed = false;
        String error = "";
        try
        {
            succeed = client.checkEmailBulk(emails);
        } catch (HttpResponseException e)
        {
            Log.d("serviceLog", "Network Error");
            error = "Network is down, Please try again later";
        } catch (Exception e1)
        {
            error = Arrays.toString(e1.getStackTrace()) + "\n" + e1.getLocalizedMessage();
        }

        Log.d("serviceLog", "Did the network succeed? " + succeed + " " + error);

        Bundle extras = intent.getExtras();

        Messenger messenger = (Messenger) extras.get("MESSENGER");
        Message msg = Message.obtain();

        if (extras != null && succeed)
        {
            // update values from network
            updateLocalMyFriends(client);
            msg.obj = (Object) "Success";

        } else
        {
            Log.v("serviceLog", "Finishing Up Error");
            msg.obj = (Object) error;
        }
        
        //send the message back to the gui thread
        try
        {
            messenger.send(msg);
        } catch (android.os.RemoteException e)
        {
            Log.v("serviceLog", "Error");
        }
    }

    private void updateLocalMyFriends(ContactCheckRequeset client)
    {
        
        Log.v("service","Update Local Contact Database");
        ArrayList<Contact> myFriendContacts = null;
        try
        {
            myFriendContacts = client.getFriends();
        } catch (Exception e)
        {
            myFriendContacts = new ArrayList<Contact>();
        }
        if (myFriendContacts == null) myFriendContacts = new ArrayList<Contact>();
        Log.v("service","My Friends Size"+myFriendContacts.size());
        for (int k = 0; k < myFriendContacts.size(); k++)
        {
            contactDB.addContact(myFriendContacts.get(k));
            Log.v("serviceLog", "Added: " + myFriendContacts.get(k).toString());
        }

    }

}
