package com.mydemo.mynewsdemo.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.activity.fragment.BaseFragment;
import com.mydemo.mynewsdemo.activity.fragment.HomeFragment;
import com.mydemo.mynewsdemo.activity.fragment.NewsFragment;
import com.mydemo.mynewsdemo.activity.fragment.ServiceFragment;
import com.mydemo.mynewsdemo.activity.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener
{

    @BindView(R.id.rg)
    RadioGroup mRadioGroup;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragments();
        mFragmentManager = getFragmentManager();
        mRadioGroup.setOnCheckedChangeListener(this);
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    /**
     * 初始化Fragments
     */
    private BaseFragment[] mFragments;

    private void initFragments()
    {
        HomeFragment homeFragment = new HomeFragment();
        NewsFragment newsFragment = new NewsFragment();
        ServiceFragment serviceFragment = new ServiceFragment();
        SettingFragment settingFragment = new SettingFragment();

        mFragments = new BaseFragment[]{homeFragment, newsFragment, serviceFragment, settingFragment};
    }

    /**
     * 隐藏或显示对应的fragment
     */
    private int lastIndex;

    private void showFragment(int fragmentIndex)
    {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        if (!mFragments[fragmentIndex].isAdded())
        {
            mTransaction.add(R.id.fl, mFragments[fragmentIndex]);
        }
        mTransaction.hide(mFragments[lastIndex]);
        mTransaction.show(mFragments[fragmentIndex]);
        lastIndex = fragmentIndex;
        mTransaction.commit();
    }

    /**
     * radioGroup切换监听
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            RadioButton child = (RadioButton) group.getChildAt(i);
            if (child.isChecked())
            {
                showFragment(i);
                break;
            }
        }
    }
}
