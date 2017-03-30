package com.mydemo.mynewsdemo.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.utils.DisplayUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chenlong on 2016/12/20.
 */

public class Banner extends RelativeLayout
{
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.points)
    LinearLayout mImagePoints;

    private Context mContext;
    private List<String> mImageUrls;
    private BannerAdapter mBannerAdapter;
    private CompositeSubscription mCompositeSubscription;
    private ImageView[] mImages;
    private FixedSpeedScroller mScroller;

    public Banner(Context context)
    {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.banner, this);
        ButterKnife.bind(this);


    }

    public class BannerAdapter extends PagerAdapter implements OnClickListener
    {
        @Override
        public int getCount()
        {
            return mImages == null ? 0 : mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            container.addView(mImages[position]);
            mImages[position].setOnClickListener(this);
            return mImages[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v)
        {

            Toast.makeText(mContext, mImages[mViewPager.getCurrentItem()]+"",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始滚动
     */

    private int delayTime = 5;
    private boolean isStopScroll;

    public void setDelayTime(int time)
    {
        delayTime = time;
    }

    private void startScroll()
    {
        isStopScroll = false;
        mCompositeSubscription = new CompositeSubscription();
        Subscription scrollSubscription = Observable.timer(delayTime, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>()
                {
                    @Override
                    public void call(Long aLong)
                    {
                        if (isStopScroll)
                            return;
                        isStopScroll = true;
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                });
        mCompositeSubscription.add(scrollSubscription);
    }

    private void stopScroll()
    {
        isStopScroll = true;
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
    }

    public void onPause()
    {
        stopScroll();
    }

    public void onResume()
    {
        startScroll();
    }

    /**
     * 设置数据后初始化 并设置适配器
     */
    private void initView()
    {
        stopScroll();

        lastPosition = 0; //初始化原点

        if (mImageUrls != null)
        {
            initImages();
        }
        initPoints();

        initScroll();

        mBannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
        mBannerAdapter.notifyDataSetChanged();

        //设置初始时选择默认的条目
        mViewPager.setCurrentItem(1);
        mImagePoints.getChildAt(0).setEnabled(false);

        //设置动画
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        //由于是add方式添加的监听 每次初始化先移除 在添加监听
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(new BannerChangeListener());

        startScroll();
    }

    /**
     * 控制ViewPager滑动速度第二步
     */
    private void initScroll()
    {
        try
        {// 自定义图片切换速度
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new FixedSpeedScroller(
                    getContext(), new AccelerateInterpolator());
            field.set(mViewPager, mScroller);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 初始化+2个长度的imageViews
     */
    private void initImages()
    {
        mImages = new ImageView[mImageUrls.size() + 2];
        for (int i = 0; i < mImages.length; i++)
        {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            if (i == 0)
            {
                Glide.with(getContext()).load(mImageUrls.get(mImageUrls.size() - 1)).into(image);
            } else if (i == mImages.length - 1)
            {
                Glide.with(getContext()).load(mImageUrls.get(0)).into(image);

            } else
            {
                Glide.with(getContext()).load(mImageUrls.get(i - 1)).into(image);

            }
            mImages[i] = image;
        }
    }

    private int lastPosition;

    /**
     * 页面移动的监听
     */
    private class BannerChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            position = (position - 1 + mImagePoints.getChildCount()) % mImagePoints.getChildCount();
            mImagePoints.getChildAt(lastPosition).setEnabled(true);
            mImagePoints.getChildAt(position).setEnabled(false);
            lastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {
            switch (state)
            {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    stopScroll();
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    if (mViewPager.getCurrentItem() == 0)
                    {
                        mViewPager.setCurrentItem(mImages.length - 2, false);    //false表示滑动无动画
                    } else if (mViewPager.getCurrentItem() == mImages.length - 1)
                    {
                        mViewPager.setCurrentItem(1, false);
                    }
                    if (isStopScroll)
                        startScroll();
                    Log.d("BannerChangeListener", mImageUrls.size() + "");
                    break;
            }
        }
    }

    /**
     * 加载小原点
     */
    private void initPoints()
    {
        mImagePoints.removeAllViews();
        for (int i = 0; i < mImages.length - 2; i++)
        {
            ImageView point = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DisplayUtil.dp2px(mContext, radius),
                    DisplayUtil.dp2px(mContext, radius));
            point.setImageDrawable(getResources().getDrawable(R.drawable.selector_point));
            params.leftMargin = DisplayUtil.dp2px(mContext, 10);
            point.setLayoutParams(params);
            mImagePoints.addView(point);
        }
    }

    private int radius = 5;

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    /**
     * 接收一个数组类型的Url
     *
     * @param imageUrls
     */
    public void setImages(String[] imageUrls)
    {
        if (imageUrls == null)
            return;
        mImageUrls = new ArrayList<>(imageUrls.length);
        for (int i = 0; i < imageUrls.length; i++)
        {
            mImageUrls.add(imageUrls[i]);
        }
        initView();
    }

    /**
     * 接收一个int数组类型的
     *
     * @param images
     */
    public void setImages(List<Integer> images, int flag)
    {
        mImages = new ImageView[images.size() + 2];
        for (int i = 0; i < mImages.length; i++)
        {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            if (i == 0)
            {
                image.setImageResource(images.get(images.size() - 1));
            } else if (i == mImages.length - 1)
            {
                image.setImageResource(images.get(0));
            } else
            {
                image.setImageResource(images.get(i - 1));
            }
            mImages[i] = image;
        }

        initView();
    }

    /**
     * 接收一个集合类型的Url
     *
     * @param imageUrls
     */
    public void setImages(List<String> imageUrls)
    {
        if (imageUrls == null)
            return;
        mImageUrls = imageUrls;
        initView();
    }

    /**
     * Google提供viewPager翻页的动画
     */
    public class DepthPageTransformer implements ViewPager.PageTransformer
    {
        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View view, float position)
        {
            int pageWidth = view.getWidth();
            if (position < -1)
            {
                view.setAlpha(0);
            } else if (position <= 0)
            {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1)
            {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                        * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else
            {
                view.setAlpha(0);

            }
        }
    }

    /**
     * 滑动类控制滑动速度第一步
     */
    private int mDuration = 500;

    public void setDuration(int time)
    {
        mDuration = time;
    }

    public class FixedSpeedScroller extends Scroller
    {

        public FixedSpeedScroller(Context context)
        {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator)
        {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration)
        {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy)
        {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

    }
}

