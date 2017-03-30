package com.mydemo.mynewsdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by chenlong on 2016/12/19.
 */
public class CommonUtil
{
    /**
     * 当前网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = getNetState(context);

        return cm.isActiveNetworkMetered();
    }

    /**
     * 封装一个ConnectivityManager 方便复用
     *
     * @param context
     * @return
     */
    public static ConnectivityManager getNetState(Context context)
    {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
