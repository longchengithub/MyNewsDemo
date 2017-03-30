package com.mydemo.mynewsdemo.utils;

import android.content.Context;

/**
 * Created by chenlong on 2016/12/19.
 */

public class DisplayUtil
{
    public static int dp2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
