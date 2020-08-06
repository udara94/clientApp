package com.example.clientapp.mvp.presenters;

import android.util.Log;

import com.example.clientapp.mvp.views.View;

import io.reactivex.observers.DisposableSingleObserver;

public class DefaultSubscriber<T> extends DisposableSingleObserver<T> {

    private String TAG = "DefaultSubscriber";
    private View mView;

    public DefaultSubscriber(View mView) {
        this.mView = mView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }
    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onError(Throwable e) {
        if(mView != null) {
            mView.showMessage(e.getLocalizedMessage());
        }
        e.printStackTrace();
        Log.e(TAG, "Error Occurred while retrieving List: " + e.getStackTrace());
    }

}
