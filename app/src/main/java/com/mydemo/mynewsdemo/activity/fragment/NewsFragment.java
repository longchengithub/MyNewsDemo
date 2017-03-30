package com.mydemo.mynewsdemo.activity.fragment;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.activity.adapter.NewsPagerAdapter;
import com.mydemo.mynewsdemo.activity.pager.BasePager;
import com.mydemo.mynewsdemo.bean.CategoriesBean;
import com.mydemo.mynewsdemo.http.RetrofitHelper;
import com.mydemo.mynewsdemo.widget.NewsCateDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenlong on 2016/12/20.
 */

public class NewsFragment extends BaseFragment implements ViewPager.OnPageChangeListener
{
    @BindView(R.id.news_tab)
    TabLayout mTabLayout;
    @BindView(R.id.news_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.news_grid)
    ImageView mNewsGrid;

    private NewsPagerAdapter mNewsAdapter;

    @Override
    public int getChildLayoutId()
    {
        return R.layout.fragment_news;
    }

    @Override
    public void initView(TextView title, ImageView search, FrameLayout frameLayout)
    {
        title.setText("新闻");
        loadCategoriesData();
    }

    private void loadCategoriesData()
    {
        RetrofitHelper.getCategoriesService().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CategoriesBean>()
                {
                    @Override
                    public void call(CategoriesBean categoriesBean)
                    {
                        mDatas = categoriesBean.getData();
                        initViewPager(filterCategoriesBean(categoriesBean));
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        Toast.makeText(getActivity(), "联网失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 过滤出符合显示条件的条目
     *
     * @param categoriesBean
     * @return
     */
    private List<CategoriesBean.DataBean> mDatas;

    private List<CategoriesBean.DataBean> filterCategoriesBean(CategoriesBean categoriesBean)
    {
        List<Integer> extend = categoriesBean.getExtend();

        List<CategoriesBean.DataBean> mShowData = new ArrayList<>();
        for (int i = 0; i < extend.size(); i++)
        {
            Integer id = extend.get(i);
            for (int j = 0; j < mDatas.size(); j++)
            {
                if (id == mDatas.get(j).getId())
                {
                    mShowData.add(mDatas.get(j));
                    break;
                }
            }
        }
        return mShowData;
    }

    private List<CategoriesBean.DataBean> mFilterData;

    private void initViewPager(List<CategoriesBean.DataBean> data)
    {
        if (data == null)
            return;
        mFilterData = data;

        mNewsAdapter = new NewsPagerAdapter(mFilterData);
        mViewPager.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();

        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager,true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    /**
     * 暂停时 同步调用当前页面自定义的pause方式 伪造一个生命周期
     */
    @Override
    public void onPause()
    {
        super.onPause();
        BasePager basePager = getCurrentBasePager();
        if (basePager != null)
            basePager.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        BasePager basePager = getCurrentBasePager();
        if (basePager != null)
            basePager.onResume();
    }

    /**
     * 页面切换时 stop上一个pager start当前pager
     */
    private int prevIndex;

    /**
     * 切换页面时,关闭上个页面的轮播图的轮播
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position)
    {
        //从当前角标拿到 当前的dataBean对象
        //通过缓存又拿到了dataBean对象对应的paper

        CategoriesBean.DataBean prevBean = mFilterData.get(prevIndex);
        CategoriesBean.DataBean currentBean = mFilterData.get(position);

        BasePager prevPager = mNewsAdapter.getCachePaper().get(prevBean);
        BasePager currentPager = mNewsAdapter.getCachePaper().get(currentBean);

        if (prevPager != null)
            prevPager.onPause();

        if (currentPager != null)
            currentPager.onResume();

        this.prevIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    private BasePager getCurrentBasePager()
    {
        int currentItem = mViewPager.getCurrentItem();
        if (mFilterData == null)        //各种非空判断
            return null;
        CategoriesBean.DataBean dataBean = mFilterData.get(currentItem);
        if (dataBean == null)
            return null;
        return mNewsAdapter.getCachePaper().get(dataBean);
    }

    /**
     * 过滤出隐藏的项目
     *
     * @return
     */
    private List<CategoriesBean.DataBean> getMHideData()
    {
        List<CategoriesBean.DataBean> mHideData = new ArrayList<>();
        mHideData.clear();
        mHideData.addAll(mDatas);
        mHideData.removeAll(mFilterData);
        return mHideData;
    }

    /**
     * 点击自定义dialog的GridLayout事件
     */
    @OnClick(R.id.news_grid)
    public void onClick()
    {
        final NewsCateDialog newsCateDialog = new NewsCateDialog(getActivity());
        newsCateDialog.setShowAndHideData(mFilterData, getMHideData());

        newsCateDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                int oldIndex = mViewPager.getCurrentItem();
                CategoriesBean.DataBean oldBean = mFilterData.get(oldIndex);

                //接收从自定义dialog筛选后 返回的数据
                List<CategoriesBean.DataBean> updateData = newsCateDialog.getUpdateData();
                initViewPager(updateData);  //根据新的data重新加载viewPager

                int newIndex = updateData.indexOf(oldBean);
                mViewPager.setCurrentItem(newIndex);
                mNewsAdapter.notifyDataSetChanged();
                mTabLayout.setupWithViewPager(mViewPager);

            }
        });
    }
}
