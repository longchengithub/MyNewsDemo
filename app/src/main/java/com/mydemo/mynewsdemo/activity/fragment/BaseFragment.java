package com.mydemo.mynewsdemo.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydemo.mynewsdemo.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenlong on 2016/12/20.
 */

public abstract class BaseFragment extends Fragment
{

    private View mRootView;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mRootView == null)
        {
            //加载根布局
            mRootView = inflater.inflate(R.layout.fragment_base, null);
            //找到控件
            TextView base_title = (TextView) mRootView.findViewById(R.id.base_title);
            ImageView base_search = (ImageView) mRootView.findViewById(R.id.base_search);
            FrameLayout base_fragment = (FrameLayout) mRootView.findViewById(R.id.base_fragment);
            /**
             * 打气的第3个参数,false 和true
             * 传入false看源码,其实和不传第三个参数一样 将布局文件打气到根布局,返回的是第一个参数自己
             * 传入true,是将布局文件打气到根布局,返回的是第二个参数根布局
             */
            base_fragment = (FrameLayout) inflater.inflate(getChildLayoutId(), base_fragment);
            initView(base_title, base_search, base_fragment);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        bind.unbind();
    }

    public abstract int getChildLayoutId();

    public abstract void initView(TextView title, ImageView search, FrameLayout frameLayout);
}
