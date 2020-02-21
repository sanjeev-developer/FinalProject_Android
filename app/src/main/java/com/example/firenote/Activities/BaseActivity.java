package com.example.firenote.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firenote.R;

public class BaseActivity extends AppCompatActivity {
    Dialog Alert;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public  void displayAlert(Context mContext, String strMessage) {

        //dialog intialization
        Alert = new Dialog(mContext);
        Alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Alert.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BaseActivity.this, R.color.transparent)));
        Alert.setContentView(R.layout.alert_diag);

        Button ok = (Button) Alert.findViewById(R.id.but_done);
        TextView alertext= (TextView) Alert.findViewById(R.id.text_alert);

        alertext.setText(strMessage);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Alert.cancel();

            }
        });

        Alert.show();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
