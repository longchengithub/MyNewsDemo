package com.mydemo.mynewsdemo;

import android.app.Application;

/**
 * Created by chenlong on 2016/12/19.
 */

public class NewsApp extends Application
{
    public static NewsApp mNewsApp;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mNewsApp = this;
    }

    public static NewsApp getInstance()
    {
        return mNewsApp;
    }
}
