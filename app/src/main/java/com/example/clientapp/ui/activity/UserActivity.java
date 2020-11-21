package com.example.clientapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class UserActivity extends Activity implements AdapterView.OnItemSelectedListener {

    final String TAG = UserActivity.this.getClass().getSimpleName();

    Spinner mSpinner;
    Button btnDone;
    int mSelectedUser = 0;

    String[] user = { "Customer", "Cashier", "Kitchen"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_user);

            mSpinner =(Spinner)findViewById(R.id.spinner);
            btnDone = (Button) findViewById(R.id.btn_done);

            mSpinner.setOnItemSelectedListener(this);

            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,user);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            mSpinner.setAdapter(aa);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoHomePage();
                }
            });


        } catch (Exception ex) {
            Log.e(TAG, "onCreate: " + ex.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mSelectedUser = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void gotoHomePage(){
        BaseApplication.getBaseApplication().setUserType(mSelectedUser);
        startActivity(new Intent(UserActivity.this, MainActivity.class));
        finish();
    }
}
