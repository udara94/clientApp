package com.example.clientapp.ui.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.google.android.material.textfield.TextInputEditText;

public class ListenerEditText extends TextInputEditText {

    private KeyImeChange keyImeChangeListener;
    public ListenerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public boolean onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null){
            return keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }
}
