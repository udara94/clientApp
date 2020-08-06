package com.example.clientapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.common.constants.IPreferencesKeys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    private static CommonUtils instance = null;

    private CommonUtils() {
    }

    public static CommonUtils getInstance() {
        if (instance == null) instance = new CommonUtils();
        return instance;
    }

    public Typeface getFont(Context mContext, String font) {
        switch (font) {
            case ApplicationConstants.FONT_ACUMIN_PRO_BOLD:
                return Typeface.createFromAsset(mContext.getAssets(), "font/Acumin Pro Bold.otf");
            case ApplicationConstants.FONT_ACUMIN_PRO_SEMIBOLD:
                return Typeface.createFromAsset(mContext.getAssets(), "font/Acumin Pro Semibold.otf");
            case ApplicationConstants.FONT_ACUMIN_PRO_REGULAR:
                return Typeface.createFromAsset(mContext.getAssets(), "font/Acumin Pro Book.otf");
            default:
                return null;
        }
    }

    public static boolean isTokenExpired(Context context) {

        boolean status = false;
        Long now = System.currentTimeMillis();
        Long expire;


        String expireString = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString(IPreferencesKeys.EXPIRE, null);
        expire = Long.parseLong(expireString);
        if (now + 120 > expire) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    public String getRefreshTOken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String refreshToken = preferences.getString(IPreferencesKeys.REFRESH_TOKEN, "");
        return refreshToken;

    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getBaseApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public String formatDate(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        return formatted;
    }

    public String getDateDifference(String postedDate) {
        String diff;
        SimpleDateFormat postedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            // Date posted = postedDateFormat.parse(postedDate);
            Date posted = postedDateFormat.parse(postedDate);
            Date nowDate = new Date();

            long different = nowDate.getTime() - posted.getTime();

            System.out.println("startDate : " + posted);
            System.out.println("endDate : " + nowDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
            diff = getSelectedDate(elapsedDays, elapsedHours, elapsedMinutes);
            return diff;

        } catch (ParseException e) {
            e.printStackTrace();
            return diff = null;
        }

    }

    private String getSelectedDate(long elapsedDays, long elapsedHours, long elapsedMinutes) {
        String diffToString;
        if (elapsedDays == 0) {
            if (elapsedHours == 0) {
                diffToString = Long.toString(elapsedMinutes) + " minutes ago";
                return diffToString;
            } else {
                diffToString = Long.toString(elapsedHours) + " hours ago";
                return diffToString;
            }
        } else {
            diffToString = Long.toString(elapsedDays) + " days ago";
            return diffToString;
        }
    }

}
