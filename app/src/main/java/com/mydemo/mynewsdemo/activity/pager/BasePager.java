package com.mydemo.mynewsdemo.activity.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.activity.adapter.CommonViewHolder;
import com.mydemo.mynewsdemo.bean.NewsPagerBean;
import com.mydemo.mynewsdemo.http.RetrofitHelper;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenlong on 2016/12/23.
 * 抽取一个父类 2个抽象方法
 * 1.传入一个分类的条目布局资源id
 * 2.让子类自己去初始化显示的条目和数据
 */

public abstract class BasePager
{
    private BaseNewsAdapter mNewsAdapter;

    public abstract int getItemLayout();

    public abstract void initItemLayout(NewsPagerBean.DataBean.NewsBean newsBean, CommonViewHolder viewHolder);

    public void onResume()
    {
    }

    public void onPause()
    {
    }

    public void initHeaderData(List<NewsPagerBean.DataBean.TopnewsBean> mTopNews)
    {
    }

    public void addHeaderView()
    {
    }

    public View mPagerView;
    public XRecyclerView mRecycler;
    public Context mContext;
    private NewsPagerBean mNewsBean;
    private List<NewsPagerBean.DataBean.TopnewsBean> mTopNews;
    private List<NewsPagerBean.DataBean.NewsBean> mNewsData;

    public View getmPagerView()
    {
        return mPagerView;
    }

    public BasePager(Context context, String url)
    {
        mContext = context;

        mPagerView = View.inflate(mContext, R.layout.item_news, null);
        mRecycler = (XRecyclerView) mPagerView.findViewById(R.id.item_recycler);

        mNewsAdapter = new BaseNewsAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        mRecycler.setLoadingMoreEnabled(true);
        mRecycler.setPullRefreshEnabled(true);

        mRecycler.setLoadingListener(new XRecyclerView.LoadingListener()
        {
            @Override
            public void onRefresh()
            {
                refreshData(mNewsBean.getData().getMore());
            }

            @Override
            public void onLoadMore()
            {
                loadMoreData(mNewsBean.getData().getMore());
            }
        });

        loadData(url);
    }

    private void loadMoreData(String more)
    {
        if (TextUtils.isEmpty(more))
        {
            Toast.makeText(mContext, "没有更多!", Toast.LENGTH_SHORT).show();
            mRecycler.loadMoreComplete();
            return;
        }
        more = more.substring(1, more.length());
        RetrofitHelper.getNewsPagersService().getNewsPagers(more)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsPagerBean>()
                {
                    @Override
                    public void call(NewsPagerBean newsPagerBean)
                    {
                        mNewsBean.getData().setMore(newsPagerBean.getData().getMore());
                        mNewsData.addAll(newsPagerBean.getData().getNews());
                        mTopNews = newsPagerBean.getData().getTopnews();

                        mNewsAdapter.notifyDataSetChanged();
                        mRecycler.loadMoreComplete();
                    }
                });
    }

    private void refreshData(String more)
    {
        if (TextUtils.isEmpty(more))
        {
            Toast.makeText(mContext, "没有更多!", Toast.LENGTH_SHORT).show();
            mRecycler.refreshComplete();
            return;
        }
        more = more.substring(1, more.length());
        RetrofitHelper.getNewsPagersService().getNewsPagers(more)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsPagerBean>()
                {
                    @Override
                    public void call(NewsPagerBean newsPagerBean)
                    {
                        mNewsBean.getData().setMore(newsPagerBean.getData().getMore());
                        mNewsData.addAll(0, newsPagerBean.getData().getNews());
                        mTopNews = newsPagerBean.getData().getTopnews();

                        initHeaderData(mTopNews);
                        mNewsAdapter.notifyDataSetChanged();
                        mRecycler.refreshComplete();
                    }
                });
    }

    /**
     * 第一次联网加载数据
     */
    private void loadData(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            Toast.makeText(mContext, "没有更多!", Toast.LENGTH_SHORT).show();
            return;
        }
        url = url.substring(1, url.length());
        RetrofitHelper.getNewsPagersService().getNewsPagers(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsPagerBean>()
                {
                    @Override
                    public void call(NewsPagerBean newsPagerBean)
                    {
                        mNewsBean = newsPagerBean;
                        mNewsData = newsPagerBean.getData().getNews();
                        mTopNews = newsPagerBean.getData().getTopnews();

                        addHeaderView();
                        initHeaderData(mTopNews);                //添加了头部

                        mRecycler.setAdapter(mNewsAdapter);     //设置适配器
                        mNewsAdapter.notifyDataSetChanged();    //刷新适配器

                    }
                });


    }

    private class BaseNewsAdapter extends RecyclerView.Adapter<CommonViewHolder>
    {
        @Override
        public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(mContext).inflate(getItemLayout(), parent, false);
            CommonViewHolder viewHolder = new CommonViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CommonViewHolder holder, int position)
        {
            initItemLayout(mNewsData.get(position), holder);
        }

        @Override
        public int getItemCount()
        {
            return mNewsData.size();
        }
    }
}
