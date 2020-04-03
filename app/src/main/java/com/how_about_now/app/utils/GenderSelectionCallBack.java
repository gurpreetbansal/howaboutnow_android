package com.how_about_now.app.utils;

import android.content.Context;

public class GenderSelectionCallBack {
    private static GenderSelectionListener listener;
    private Context context;
    private static GenderSelectionCallBack buttonCallBack;

    private GenderSelectionCallBack(Context context) {
        this.context = context;
    }  //private constructor.

    public static GenderSelectionCallBack getInstance(Context context) {
        if (buttonCallBack == null) { //if there is no instance available... create new one
            buttonCallBack = new GenderSelectionCallBack(context);
        }
        return buttonCallBack;
    }

    public void onGenderSelectionListener(String gender) {
        if (listener != null) {
            listener.onGenderSelectionCallBack(gender);
        }
    }

    public void setGenderSelectionListener(GenderSelectionListener listener) {
        this.listener = listener;
    }

    public interface GenderSelectionListener {
        public void onGenderSelectionCallBack(String gender);
    }
}