package com.mydemo.mynewsdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenlong on 2016/12/19.
 */

public class SPUtil
{
    private static SharedPreferences sp;

    private static SharedPreferences getSP(Context context)
    {
        if (sp == null)
        {
            sp = context.getSharedPreferences(SPConstant.CONFIG, Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static boolean getBoolean(Context context, String key)
    {
        return getSP(context).getBoolean(key, true);
    }

    public static void putBoolean(Context context, String key, boolean value)
    {
        getSP(context).edit().putBoolean(key, value).commit();
    }
}
