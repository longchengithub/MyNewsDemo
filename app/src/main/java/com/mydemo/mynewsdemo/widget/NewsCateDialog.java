package com.mydemo.mynewsdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.bean.CategoriesBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlong on 2016/12/24.
 */

public class NewsCateDialog extends Dialog
{
    @BindView(R.id.dragLayout)
    DragLayout mDragLayout;

    public NewsCateDialog(Context context)
    {
        //一定要设置主题,不然好像无法全屏之类的 麻烦
        super(context, R.style.cate_arr_style);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        getWindow().setAttributes(params);

        setCanceledOnTouchOutside(true);

        View view = View.inflate(context, R.layout.news_categories_dialog, null);
        setContentView(view);
        ButterKnife.bind(this);

        show();
    }

    //通过键值对,将数据与标题绑定. 当GridLayout返回的所有标题作为键,可以找出所有的值
    private Map<String, CategoriesBean.DataBean> allData = new HashMap<>();

    public void setShowAndHideData(List<CategoriesBean.DataBean> mShowDatas, List<CategoriesBean.DataBean> mHideDatas)
    {
        //保存显示的条目,并保存到map集合 用于返回数据时 提供给dialog层 具体显示的bean对象
        List<String> mShowTitle = new ArrayList<>();
        for (CategoriesBean.DataBean mShowData : mShowDatas)
        {
            mShowTitle.add(mShowData.getTitle());
            allData.put(mShowData.getTitle(), mShowData);
        }

        //保存隐藏的条目,并保存到map集合
        List<String> mHideTitle = new ArrayList<>();
        for (CategoriesBean.DataBean mHideData : mHideDatas)
        {
            mHideTitle.add(mHideData.getTitle());
            allData.put(mHideData.getTitle(), mHideData);
        }
        mDragLayout.setShowAndHideItems(mShowTitle, mHideTitle);

    }

    public List<CategoriesBean.DataBean> getUpdateData()
    {
        List<CategoriesBean.DataBean> updateData = new ArrayList<>();
        List<String> showData = mDragLayout.getShowData();
        for (String title : showData)
        {
            updateData.add(allData.get(title));
        }
        return updateData;
    }

}
