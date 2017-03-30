package com.mydemo.mynewsdemo.activity.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydemo.mynewsdemo.R;

/**
 * Created by chenlong on 2016/12/20.
 */

public class ServiceFragment extends BaseFragment
{
    @Override
    public int getChildLayoutId()
    {
        return R.layout.fragment_service;
    }

    @Override
    public void initView(TextView title, ImageView search, FrameLayout frameLayout)
    {
        title.setText("服务");
        search.setVisibility(View.GONE);
    }

}
