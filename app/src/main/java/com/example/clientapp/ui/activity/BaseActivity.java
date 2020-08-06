package com.example.clientapp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.clientapp.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class BaseActivity extends AppCompatActivity {
    public static BaseActivity baseActivity = null;

    final String TAG = BaseActivity.this.getClass().getSimpleName();
    //    protected Presenter presenter;
    protected Toolbar mToolBar;

    public AlertDialog myAlertDialog;
    AlertDialog alertDialog = null;
    protected ImageView img = null;
    private KProgressHUD pd = null;


    public static boolean isAppWentToBg = true;

    public static boolean isWindowFocused = false;

    public static boolean isBackPressed = false;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // getActionToolbar();
        baseActivity = this;
    }

    protected androidx.appcompat.widget.Toolbar getActionBarToolbar() {
        if (mToolBar == null) {
            mToolBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
            if (mToolBar != null) {
                setSupportActionBar(mToolBar);
                ActionBar mActionBar = BaseActivity.this.getSupportActionBar();
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
                mActionBar.setDisplayShowCustomEnabled(true);

                // remove previously created actionbar
                mActionBar.invalidateOptionsMenu();

                /** remove actionbar unnecessary left margin */
                mToolBar.setContentInsetsAbsolute(0, 0);
            }
        }
        return mToolBar;
    }

    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
        }
    }

    public void applicationdidenterbackground() {
        if (!isWindowFocused) {
            isAppWentToBg = true;
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);
        applicationWillEnterForeground();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop ");
        applicationdidenterbackground();
    }

    @Override
    public void onBackPressed() {

        if (this instanceof MainActivity) {
        } else {
            isBackPressed = true;
        }
        Log.d(TAG, "onBackPressed " + isBackPressed + "" + this.getLocalClassName());
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        isWindowFocused = hasFocus;
        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }

        super.onWindowFocusChanged(hasFocus);
    }

    protected void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Ok", defaultDialogClickListener());

        if (!BaseActivity.this.isFinishing()) alertDialog.show();
    }

    protected DialogInterface.OnClickListener defaultDialogClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        };
    }

    public void setProgressDialog(boolean isLoading) {
        try {
            if (isLoading) {
                if (pd != null) pd.show();
                else pd = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(true)
                        .setLabel("Please wait")
                        .setAnimationSpeed(1)
                        .setDimAmount(0.3f)
                        .show();

            } else {
                if (pd != null) pd.dismiss();
            }
        } catch (Exception e) {
            Log.e("BaseFragment", "setProgressDialog: " + e.toString());
        }
    }
}
