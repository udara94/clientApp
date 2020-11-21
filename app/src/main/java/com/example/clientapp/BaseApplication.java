package com.example.clientapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import lombok.Getter;
import lombok.Setter;

public class BaseApplication extends MultiDexApplication {
    private static BaseApplication baseApplication;

    private final String TAG = BaseApplication.this.getClass().getSimpleName();

    @Getter
    @Setter
    private boolean appIsBackground = true;


    @Getter
    @Setter
    private boolean loadItemDetailsScreen = false;

    @Getter
    @Setter
    private boolean loadNotificationScreen = false;

    @Getter
    @Setter
    private boolean loadOrderDetailsScreen = false;

    @Getter
    @Setter
    private boolean loadPaymentDetailsScreen = false;

    @Getter
    @Setter
    private boolean loadCashlessScreen = false;

    @Getter
    @Setter
    private boolean loadApplication = false;

    @Getter
    @Setter
    private int userType = 0;



    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = (BaseApplication) getApplicationContext();
    }

    public BaseApplication(){
        super();
    }

    public static BaseApplication getBaseApplication(){
        return  baseApplication;
    }

    public void close(Context context) {
        try {
            //clearData();
            ((Activity) context).finish();
        } catch (Exception e) {
            Log.e(TAG, "exTokenClearData: " + e.toString());
        }
    }
}
