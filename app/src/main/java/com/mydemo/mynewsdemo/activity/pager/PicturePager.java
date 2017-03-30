package com.mydemo.mynewsdemo.activity.pager;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.activity.adapter.CommonViewHolder;
import com.mydemo.mynewsdemo.bean.NewsPagerBean;
import com.mydemo.mynewsdemo.http.api.ApiConstant;

/**
 * Created by chenlong on 2016/12/23.
 */

public class PicturePager extends BasePager
{
    public PicturePager(Context context, String url)
    {
        super(context, url);
    }

    @Override
    public int getItemLayout()
    {
        return R.layout.picturepager_item_layout;
    }

    @Override
    public void initItemLayout(NewsPagerBean.DataBean.NewsBean newsBean, CommonViewHolder viewHolder)
    {
        Glide.with(mContext).load(ApiConstant.URL + newsBean.getListimage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.getImageView(R.id.pic_iv_icon));
        viewHolder.getTextView(R.id.pic_tv_title).setText(newsBean.getTitle());
    }
}
