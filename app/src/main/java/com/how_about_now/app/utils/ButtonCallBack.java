package com.how_about_now.app.utils;

import android.content.Context;

public class ButtonCallBack {
    private static LoginButtonListener listner;
    private Context context;
    private static ButtonCallBack buttonCallBack;

    private ButtonCallBack(Context context) {
        this.context = context;
    }  //private constructor.

    public static ButtonCallBack getInstance(Context context) {
        if (buttonCallBack == null) { //if there is no instance available... create new one
            buttonCallBack = new ButtonCallBack(context);
        }
        return buttonCallBack;
    }

    public void setLoginButtonListener(LoginButtonListener listner) {
        this.listner = listner;
    }

    public interface LoginButtonListener {
        public void loginButtonCallBack();
    }
}