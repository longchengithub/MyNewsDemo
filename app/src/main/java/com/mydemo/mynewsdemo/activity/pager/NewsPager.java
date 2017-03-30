package com.mydemo.mynewsdemo.activity.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.activity.NewsDetailActivity;
import com.mydemo.mynewsdemo.activity.adapter.CommonViewHolder;
import com.mydemo.mynewsdemo.bean.NewsPagerBean;
import com.mydemo.mynewsdemo.http.api.ApiConstant;
import com.mydemo.mynewsdemo.widget.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlong on 2016/12/23.
 */

public class NewsPager extends BasePager
{
    private Banner mBanner;
    private int prevIndex = 0;

    public NewsPager(Context context, String url)
    {
        super(context, url);

        /**
         * 滚动的监听 判断如果是往下 第一个条目被隐藏了 就不滚动 显示了就滚动
         */
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecycler.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                //
                if (prevIndex != firstVisibleItemPosition)
                {
                    if (firstVisibleItemPosition > 2)
                    {
                        onPause();
                    } else
                    {
                        onResume();
                    }
                    prevIndex = firstVisibleItemPosition;
                }
            }
        });
    }

    @Override
    public int getItemLayout()
    {
        return R.layout.pager_listview_item;
    }

    @Override
    public void initItemLayout(final NewsPagerBean.DataBean.NewsBean newsBean, final CommonViewHolder viewHolder)
    {
        Glide.with(mContext).load(ApiConstant.URL + newsBean.getListimage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.getImageView(R.id.pageList_iv_icon));
        viewHolder.getTextView(R.id.pageList_tv_title).setText(newsBean.getTitle());
        viewHolder.getTextView(R.id.pageList_tv_pubdate).setText(newsBean.getPubdate());

        final String newsId = mContext.getSharedPreferences("config", Context.MODE_PRIVATE)
                .getString("newsId", "");
        final boolean isRead = newsId.contains(newsBean.getId() + "-");
        viewHolder.getTextView(R.id.pageList_tv_title).setTextColor(
                isRead ? Color.GRAY : Color.BLACK);

        //点击的回调
        viewHolder.getRelativeLayout(R.id.news_pager).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!isRead)
                {
                    mContext.getSharedPreferences("config", Context.MODE_PRIVATE)
                            .edit().putString("newsId", newsId + newsBean.getId() + "-").commit();
                    viewHolder.getTextView(R.id.pageList_tv_title).setTextColor(Color.GRAY);
                }

                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("detail", newsBean.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void initHeaderData(List<NewsPagerBean.DataBean.TopnewsBean> mTopNews)
    {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < mTopNews.size(); i++)
        {
            results.add(ApiConstant.URL + mTopNews.get(i).getTopimage());
        }
        mBanner.setImages(results);
    }

    @Override
    public void addHeaderView()
    {
        View view = View.inflate(mContext, R.layout.recycler_header, null);
        mBanner = ((Banner) view.findViewById(R.id.news_banner));
        mRecycler.addHeaderView(view);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mBanner != null)
            mBanner.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mBanner != null)
            mBanner.onPause();
    }

}
