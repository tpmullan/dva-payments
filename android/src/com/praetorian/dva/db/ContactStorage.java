package com.praetorian.dva.db;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

/**
 * Contact Storage Database Adapter
 * 
 * Puts contacts into a database for the my friends feature of the applicaion.
 * 
 * @author Richard Penshorn
 * 
 */
public class ContactStorage extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "com.praeatorian.dva.contacts";
    private static final String TABLE_USER_CONTACT = "usercontacts";

    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NULL = "";

    private static final String KEY_SECRET = "thereisnothingtoseehere,nowgoawayorIshalltauntyouasecondtime";
    private static final boolean DISABLE_SECRET = true;
    private SecretKey key = null;

    public ContactStorage(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub

    }

    /**
     * Creates new contact storage object. Needs current running context from
     * the activity or fragment which the instance will live.
     * 
     * @param context
     */
    public ContactStorage(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try
        {
            DESKeySpec keySpec = new DESKeySpec(KEY_SECRET.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeySpecException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USER_PREF_TABLE = "CREATE TABLE " + TABLE_USER_CONTACT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERNAME
                + " TEXT," + KEY_NAME + " TEXT," + KEY_EMAIL + ")";
        db.execSQL(CREATE_USER_PREF_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_CONTACT);
        onCreate(db);

    }

    /**
     * Checks if a contact exist
     * 
     * @param name
     *            (Name of contact to check)
     * @return
     */
    public boolean hasContact(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String value = "SELECT " + KEY_ID + " FROM " + TABLE_USER_CONTACT + " WHERE " + KEY_NAME + "='" + encode(name) + "'";

        Cursor cursor = db.rawQuery(value, null);
        cursor.moveToFirst();
        boolean result = false;
        if (cursor.getCount() != 0)
        {
            result = true;
        }
        db.close();
        return result;

    }

    /**
     * Adds a contact to the contact storage
     * 
     * @param name
     *            (Name of contact)
     * @param username
     *            (Username of Contact)
     * @param email
     *            (Email of contact)
     */
    public void addContact(String name, String username, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String addValue = "INSERT INTO " + TABLE_USER_CONTACT + " (" + KEY_NAME + "," + KEY_USERNAME + "," + KEY_EMAIL + ") VALUES ('" + encode(name)
                + "','" + encode(username) + "','" + encode(email) + "')";
        db.execSQL(addValue);
        db.close();

    }

    /**
     * Destroys Contact Database, useful for cleaning up personal information
     */
    public void cleanContact()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_CONTACT);
        onCreate(db);
        db.close();
    }

    public void addContact(Contact person)
    {
        addContact(person.getName(), person.getUsername(), person.getEmail());
    }

    /**
     * Returns a ArrayList<Contact> object with all contacts from datase
     * 
     * @return ArrayList<Contact>
     */
    public ArrayList<Contact> getContacts()
    {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        String getValues = "SELECT " + KEY_NAME + "," + KEY_USERNAME + "," + KEY_EMAIL + " FROM " + TABLE_USER_CONTACT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(getValues, null);

        c.moveToFirst();

        if (c.getCount() == 0)
        {
            return contacts;
        }
        while (!c.isLast())
        {
            contacts.add(new Contact(decode(c.getString(0)), decode(c.getString(1)), decode(c.getString(2))));
            c.moveToNext();
        }
        contacts.add(new Contact(decode(c.getString(0)), decode(c.getString(1)), decode(c.getString(2))));
        db.close();
        return contacts;

    }

    /**
     * Encodes data so only the database program understands the encrypt data
     * 
     * @param data
     *            (Clear texts)
     * @return encrypted texts
     */
    private String encode(String data)
    {
        if (DISABLE_SECRET) return data;

        byte[] cleartext;
        try
        {
            cleartext = data.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Decryptes data from the database.
     * 
     * @param data
     *            (Encrypted Texts)
     * @return (Clear Texts)
     */
    private String decode(String data)
    {
        if (DISABLE_SECRET) return data;

        byte[] encrypedPwdBytes = Base64.decode(data, Base64.DEFAULT);

        Cipher cipher;
        try
        {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encrypedPwdBytes));
        } catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Deletes the database
     */
    public void destoryDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_CONTACT);
        onCreate(db);
        db.close();
    }

}
