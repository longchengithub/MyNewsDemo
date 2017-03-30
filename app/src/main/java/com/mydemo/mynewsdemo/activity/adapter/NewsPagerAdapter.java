package com.mydemo.mynewsdemo.activity.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mydemo.mynewsdemo.activity.pager.BasePager;
import com.mydemo.mynewsdemo.activity.pager.NewsPager;
import com.mydemo.mynewsdemo.activity.pager.PicturePager;
import com.mydemo.mynewsdemo.bean.CategoriesBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlong on 2016/12/21.
 */

public class NewsPagerAdapter extends PagerAdapter
{
    private List<CategoriesBean.DataBean> mData;

    public NewsPagerAdapter(List<CategoriesBean.DataBean> data)
    {
        mData = data;
    }

    @Override
    public int getCount()
    {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }


    /**
     * 缓存所有的viewPager 避免3个页面之后 移除了viewpager 重新创建
     */
    private Map<CategoriesBean.DataBean, BasePager> cachePaper = new HashMap<>();

    public Map<CategoriesBean.DataBean, BasePager> getCachePaper()
    {
        return cachePaper;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        CategoriesBean.DataBean dataBean = mData.get(position);
        BasePager basePager = null;

        if (cachePaper.get(dataBean) == null)       //如果缓存中没有这个键值对匹配的basePager就添加
        {
            if (mData.get(position).getType() == 2)     //没有就new
            {
                basePager = new PicturePager(container.getContext(), dataBean.getUrl());
            } else
            {
                basePager = new NewsPager(container.getContext(), dataBean.getUrl());
            }
            cachePaper.put(dataBean, basePager);         //new之后存入集合
        }
        basePager = cachePaper.get(dataBean);           //从集合中拿出来复用
        View pagerView = basePager.getmPagerView();
        container.addView(pagerView);
        return pagerView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mData.get(position).getTitle();
    }
}
