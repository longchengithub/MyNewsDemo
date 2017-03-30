package com.mydemo.mynewsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.mydemo.mynewsdemo.activity.GuideActivity;
import com.mydemo.mynewsdemo.activity.MainActivity;
import com.mydemo.mynewsdemo.utils.SPConstant;
import com.mydemo.mynewsdemo.utils.SPUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SplashActivity extends Activity
{

    @BindView(R.id.iv_splash)
    ImageView mSplashImage;
    private CompositeSubscription mCompositeSubscription;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        startSplashAnimation();
    }

    private void startSplashAnimation()
    {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1,
                0, 1,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(2000);
        mSplashImage.setAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                mCompositeSubscription = new CompositeSubscription();
                mSubscription = Observable.timer(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>()
                        {
                            @Override
                            public void call(Long aLong)
                            {
                                enterGuildOrHome();
                            }
                        });
                mCompositeSubscription.add(mSubscription);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    /**
     * 判断跳转向导页面还是主页面
     */
    private void enterGuildOrHome()
    {
        boolean isFirst = SPUtil.getBoolean(getBaseContext(), SPConstant.IS_FIRST);
        Intent intent;
        if (isFirst)    //是第一次就进向导页面
        {
            intent = new Intent(getBaseContext(), GuideActivity.class);
        } else          //不是就直接进主页面
        {
            intent = new Intent(getBaseContext(), MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (mSubscription != null)
            mCompositeSubscription.remove(mSubscription);
    }
}
