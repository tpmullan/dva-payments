package com.praetorian.dva.popup;

import com.praetorian.dva.R;
import com.praetorian.dva.db.PrefStorage;
import com.praetorian.dva.fragments.LoginFragment;
import com.praetorian.dva.fragments.MyAccountFragment;
import com.praetorian.dva.misc.Constants;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
/**
 * Popup Controller for changing the password
 * @author richard
 *
 */
public class PasswordChangerPopup implements OnClickListener
{
    public static final String KEY_UPDATE_PASSWORD = "updated_password";
    private Context context;
    private View popupView;
    PopupWindow popupWindow;
    private EditText masterPassword;
    private EditText currentPassword;
    private EditText password1;
    private EditText password2;
    private Button update;
    private Button close;
    PrefStorage prefs;

    public PasswordChangerPopup(View popupView, Context context)
    {
        this.context = context;
        this.popupView = popupView;

        prefs = new PrefStorage(context);
        popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        update = (Button) popupView.findViewById(R.id.popupPassword_button);
        close = (Button) popupView.findViewById(R.id.popup_password_close_button);
        masterPassword = (EditText) popupView.findViewById(R.id.myaccount_password_edittext);
        password1 = (EditText) popupView.findViewById(R.id.popup_password1_edittext);
        password2 = (EditText) popupView.findViewById(R.id.popup_password2_edittext);
        
        popupWindow.setOutsideTouchable(true);
       

        update.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    public void show(View page, int x, int y)
    {

        if (!isPopping())
        {
            popupWindow.setFocusable(true);
            popupWindow.showAsDropDown(page, x, y);
            

        }

    }


    @Override
    public void onClick(View arg0)
    {
    	switch(arg0.getId())
    	{
    	case R.id.popupPassword_button:
    		 if (password1.getText().toString().equals(password2.getText().toString()))
    	        {
    	            prefs.updateValue(Constants.KEY_PASSWORD_OLD, password1.getText().toString());
    	            popupWindow.dismiss();
    	        } else
    	        {
    	            showToastMessage("Password invalid, Please try again");

    	        }
    		 break;
    	case R.id.popup_password_close_button:
    		popupWindow.dismiss();
    	}
       

    }

    private void showToastMessage(String str)
    {
        Context context = popupView.getContext();
        Toast message = Toast.makeText(context, str, 3);
        message.show();

    }

    public boolean isPopping()
    {
        Log.d("popup", "" + popupWindow.isShowing());
        return popupWindow.isShowing();
    }
}
