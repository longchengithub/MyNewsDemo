package com.mydemo.mynewsdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.utils.DisplayUtil;
import com.mydemo.mynewsdemo.utils.SPConstant;
import com.mydemo.mynewsdemo.utils.SPUtil;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlong on 2016/12/19.
 */
public class GuideActivity extends Activity
{
    @BindView(R.id.vp_guild)
    ViewPager mGuildViewPager;
    @BindView(R.id.indicator_guild)
    CirclePageIndicator mIndicatorGuild;

    private GuildAdapter mGuildAdapter;
    private int[] pics = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild);
        ButterKnife.bind(this);
        mGuildAdapter = new GuildAdapter();
        mGuildViewPager.setAdapter(mGuildAdapter);

        mIndicatorGuild.setRadius(DisplayUtil.dp2px(getBaseContext(), 5));
        mIndicatorGuild.setFillColor(Color.RED);
        mIndicatorGuild.setPageColor(Color.WHITE);
        mIndicatorGuild.setStrokeColor(Color.TRANSPARENT);
        mIndicatorGuild.setViewPager(mGuildViewPager);
    }

    private class GuildAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return pics.length + 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            if (position == getCount() - 1)
            {
                View lastView = View.inflate(getBaseContext(), R.layout.guild_last, null);
                View first = lastView.findViewById(R.id.tv_first);
                first.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        SPUtil.putBoolean(GuideActivity.this, SPConstant.IS_FIRST, false);
                        startActivity(new Intent(GuideActivity.this, MainActivity.class));
                        finish();
                    }
                });
                container.addView(lastView);
                return lastView;
            } else
            {
                ImageView mImageView = new ImageView(getBaseContext());
                mImageView.setImageResource(pics[position]);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                container.addView(mImageView);
                return mImageView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }
}
