package com.mydemo.mynewsdemo.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlong on 2016/12/23.
 */

public class CommonViewHolder extends RecyclerView.ViewHolder
{
    public Map<Integer, View> widgets = new HashMap<>();

    private View convertView;

    public CommonViewHolder(View convertView)
    {
        super(convertView);
        this.convertView = convertView;
    }

    public void putView(int itemId, View view)
    {
        widgets.put(itemId, view);
    }

    //1.根据xml中的资源ID返回对应的控件 返回为View类型
    private View getView(int itemId)
    {
        if (widgets.get(itemId) == null)
        {
            putView(itemId, convertView.findViewById(itemId));
        }
        return widgets.get(itemId);
    }

    //2.泛型增强,通过参数的重载,预先进行强制转换类型
    private <T extends View> T getView(int itemId, Class<T> clazz)
    {
        return (T) getView(itemId);
    }

    //3.通过调用泛型2,2调用1, 直接返回一个指定类型的TextView
    public TextView getTextView(int itemId)
    {
        return getView(itemId, TextView.class);
    }

    public ImageView getImageView(int itemId)
    {
        return getView(itemId, ImageView.class);
    }

    public RelativeLayout getRelativeLayout(int layoutId){
        return getView(layoutId,RelativeLayout.class);
    }
}
