package com.praetorian.dva.db;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
import android.util.Log;
/**
 * Pref Storage, for storing shared preferences for GUI, Network, and backend elements. 
 * 
 * Encrypts data that is stored in the database using DES to prevent dataleakage. 
 * @author Richard Penshorn
 *
 */
public class PrefStorage extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "com.praeatorian.dva";
    private static final String TABLE_USER_PREF = "userprefs";

    private static final String KEY_ID = "id";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NULL = "";
    
    private static final String KEY_SECRET = "thereisnothingtoseehere,nowgoawayorIshalltauntyouasecondtime";
    private static final boolean DISABLE_SECRET = true;
    private SecretKey key = null;
    
    private Context context;

    

    public PrefStorage(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub

    }

    public PrefStorage(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
        String CREATE_USER_PREF_TABLE = "CREATE TABLE " + TABLE_USER_PREF + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_KEY + " TEXT,"
                + KEY_VALUE + " TEXT" + ")";
        db.execSQL(CREATE_USER_PREF_TABLE);
       
    }
    
    public void destoryDB()
    {
        AccountStorage as = new AccountStorage(context);
        as.destory();
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PREF);
        onCreate(db);

    }

    public String getValue(String key)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String getValue = "SELECT value FROM " + TABLE_USER_PREF + " WHERE KEY = '" + encode(key) + "'";
        Cursor cursor = db.rawQuery(getValue, null);
        if (cursor.getCount() == 0) return NULL;
        cursor.moveToFirst();
        String output = cursor.getString(0);
        db.close();
        return decode(output);

    }
    public void removeValue(String key)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_USER_PREF + " WHERE " + KEY_KEY + "='" + encode(key) + "'";
        db.execSQL(sql);
        db.close();
    }
    public boolean addValue(String key, String value)
    {
        Log.d("sql","addValue: Key: " + key + "  Value: " +value);
        if (hasValue(key))
        {
            return false;
        }
        Log.println(Log.VERBOSE, "db", "adding userinformation to the cred store");
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.execSQL("INSERT INTO " + TABLE_USER_PREF + " (" + KEY_KEY + "," + KEY_VALUE + ") VALUES ('" + encode(key) + "','" + encode(value) + "')"); 

        db.close();
        return true;

    }

    public boolean setValue(String key, String value)
    {
        Log.d("sql","setValue: Key: " + key + "   Value: " + value + "\n"+"UPDATE " + TABLE_USER_PREF + " SET " + KEY_VALUE+"='" + value + "' WHERE " + KEY_KEY + "='" + key + "'");
        
        if (!hasValue(key))
        {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor output = null;
        try{
        output = db.rawQuery("UPDATE " + TABLE_USER_PREF + " SET " + KEY_VALUE+"='" + encode(value) + "' WHERE " + KEY_KEY + "='" + encode(key) + "'", null);
        output.moveToFirst();
        output.close();
        }
        catch(Exception e)
        {
            Log.d("sql",e.getMessage());
        }
        db.close();
        
        
        
        return true;


    }

    public boolean hasValue(String key)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PREF, new String[]
        { KEY_KEY, KEY_VALUE }, KEY_KEY + "=?", new String[]
        { encode(key) }, null, null, null, null);
        boolean result = false;
        if (cursor.getCount() != 0)
        {
            result = true;
        }
        db.close();
        return result;
    }

    public void updateValue(String key, String value)
    {
        if (hasValue(key))
        {
            setValue(key, value);
        } else
        {
            addValue(key, value);
        }
    }
    private String encode(String data)
    {
        if(DISABLE_SECRET)
            return data;
        
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
    private String decode(String data)
    {
        if(DISABLE_SECRET)
            return data;
        
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
}
