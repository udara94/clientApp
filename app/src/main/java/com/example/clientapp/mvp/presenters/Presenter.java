package com.example.clientapp.mvp.presenters;

import com.example.clientapp.mvp.views.View;

public interface Presenter {

    void onCreate();
    void onStart();
    void onDestroy();
    void attachView(View v);
    void onStop();
}
