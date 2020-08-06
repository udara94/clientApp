package com.example.clientapp.mvp.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.clientapp.BaseApplication;
import com.example.clientapp.common.constants.ApplicationConstants;
import com.example.clientapp.common.constants.IPreferencesKeys;
import com.example.clientapp.domain.Service;
import com.example.clientapp.model.rest.exception.RetrofitException;
import com.example.clientapp.mvp.views.View;
import com.example.clientapp.utils.IScheduler;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter implements Presenter {

    protected Activity activity;
    protected Service mService;
    protected CompositeDisposable disposable = new CompositeDisposable();

    protected IScheduler scheduler;
    protected View mView;

    protected SharedPreferences preferences;
    private String access_token;
    private String user_id;

    protected BasePresenter(Activity activityContext, Service pService, IScheduler scheduler){
        this.activity = activityContext;
        this.mService = pService;
        this.scheduler = scheduler;

        this.preferences = activityContext.getSharedPreferences(activityContext.getPackageName(), Context.MODE_PRIVATE);
        this.access_token = preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
    }

    public String getAccessToken() {
        if (access_token == null || access_token.equals("")) {
            Context mContext = BaseApplication.getBaseApplication();
            SharedPreferences preferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            String token =  preferences.getString(IPreferencesKeys.ACCESS_TOKEN, "");
            return token;
        } else {
            return  access_token;
        }
    }

    public String getExceptionMessage(Throwable e) {
        if (((RetrofitException) e).getKind() == RetrofitException.Kind.NETWORK) {
            return ApplicationConstants.ERROR_MSG_REST_NETWORK;
        } else if (((RetrofitException) e).getKind() == RetrofitException.Kind.HTTP) {
            return ApplicationConstants.ERROR_MSG_REST_HTTP;
        } else if (((RetrofitException) e).getKind() == RetrofitException.Kind.UNEXPECTED) {
            return ApplicationConstants.ERROR_MSG_REST_UNEXPECTED;
        }
        return null;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(View v) {

    }

    @Override
    public void onStop() {

    }
}
