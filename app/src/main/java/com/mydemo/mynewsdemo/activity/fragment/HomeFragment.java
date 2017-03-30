package com.mydemo.mynewsdemo.activity.fragment;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.http.api.ApiConstant;
import com.mydemo.mynewsdemo.widget.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenlong on 2016/12/20.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
{
    @BindView(R.id.home_swipe)
    SwipeRefreshLayout mHomeSwipe;
    @BindView(R.id.main_nav_view)
    NavigationView mNavView;
    @BindView(R.id.home_tianqi)
    ImageView mTianQi;
    @BindView(R.id.home_drawer)
    DrawerLayout mDrawer;

    private Banner mBanner;

    @Override
    public int getChildLayoutId()
    {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(TextView title, ImageView search, FrameLayout frameLayout)
    {
        title.setText("首页");

        mBanner = (Banner) frameLayout.findViewById(R.id.home_banner);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(ApiConstant.URL + "/10007/1452327318UU91.jpg");
        imageUrls.add(ApiConstant.URL + "/10007/1452327318UU92.jpg");
        imageUrls.add(ApiConstant.URL + "/10007/1452327318UU93.jpg");
        imageUrls.add(ApiConstant.URL + "/10007/1452327318UU94.png");
        mBanner.setImages(imageUrls);


    }

    private void initSwipe()
    {
//        mHomeSwipe.setProgressBackgroundColorSchemeColor();   设置背景色
        mHomeSwipe.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);                  //设置进度条颜色 最多4种
        mHomeSwipe.setOnRefreshListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mBanner.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mBanner.onResume();

        initSwipe();
        initDrawer();
        initNavigation();
        initTianQi();
    }

    private void initDrawer()
    {
    }

    private void initTianQi()
    {
        mTianQi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mDrawer.isDrawerOpen(mNavView))
                {
                    mDrawer.closeDrawer(mNavView);
                } else
                {
                    mDrawer.openDrawer(mNavView);
                }
            }
        });
    }

    private void initNavigation()
    {
    }

    @Override
    public void onRefresh()
    {
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>()
                {
                    @Override
                    public void call(Long aLong)
                    {
                        mHomeSwipe.setRefreshing(false);
                    }
                });
    }

}
