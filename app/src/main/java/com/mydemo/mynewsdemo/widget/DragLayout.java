package com.mydemo.mynewsdemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlong on 2016/12/25.
 */

public class DragLayout extends LinearLayout implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener
{
    private final int mScreenWith;
    @BindView(R.id.show_grid)
    GridLayout mShowGrid;
    @BindView(R.id.hide_grid)
    GridLayout mHideGrid;

    private Context mContext;

    public DragLayout(Context context)
    {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.news_categories_draglayout, this);
        ButterKnife.bind(this);

        mContext = context;
        mScreenWith = context.getResources().getDisplayMetrics().widthPixels;

        mShowGrid.setOnDragListener(this);
    }

    /**
     * 返回所有显示的条目的text属性
     *
     * @return
     */
    public List<String> getShowData()
    {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < mShowGrid.getChildCount(); i++)
        {
            data.add(((TextView) mShowGrid.getChildAt(i)).getText().toString());
        }
        return data;
    }

    /**
     * 对外提供了一个设置参数的方法
     *
     * @param showItems
     * @param hideItems
     */
    public void setShowAndHideItems(List<String> showItems, List<String> hideItems)
    {
        for (String showItem : showItems)
        {

            addItems(showItem, mShowGrid);
        }


        for (String hideItem : hideItems)
        {

            addItems(hideItem, mHideGrid);
        }

    }

    /**
     * 增加TextView到对应的gridLayout
     *
     * @param title
     * @param layout
     */
    private void addItems(String title, GridLayout layout)
    {
        TextView textView = getTextView(title);  //获取一个TextView
        layout.addView(textView);               //添加到对应的显示或隐藏的gridLayout

        textView.setOnClickListener(this);      //单机
        textView.setOnLongClickListener(this);      //长按
    }

    /**
     * 创建一个textView 设置其相关的宽高等属性 并返回
     *
     * @param title
     * @return
     */
    private TextView getTextView(String title)
    {
        TextView textview = new TextView(mContext);
        //父节点是GridLayout 拿到params 获取屏幕的宽度 除以4减去2边的margin边距
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = mScreenWith / 4 - DisplayUtil.dp2px(mContext, 5) * 2;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(DisplayUtil.dp2px(mContext, 5), DisplayUtil.dp2px(mContext, 5)
                , DisplayUtil.dp2px(mContext, 5), DisplayUtil.dp2px(mContext, 5));

        //给TextView设置相关的属性
        textview.setLayoutParams(params);
        textview.setTextSize(18);
        textview.setGravity(Gravity.CENTER);
        textview.setBackgroundResource(R.drawable.shape_dialog_enable);
        textview.setText(title);
        return textview;
    }

    /**
     * 单击点击的处理
     *
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        if (v.getParent() == mShowGrid)
        {
            if (mShowGrid.getChildCount() > 1)      //至少要保留一个
            {
                mShowGrid.removeView(v);
                //★★★不能直接用addView的方式添加,因为有动画的原因,会报错冲突 已有一个parent★★★
                //所以只能重新添加一个崭新的view ,复用了移除的view的getText显示的文本
                addItems(((TextView) v).getText().toString(), mHideGrid);
            }
        }
        if (v.getParent() == mHideGrid)
        {
            mHideGrid.removeView(v);
            addItems(((TextView) v).getText().toString(), mShowGrid);
        }
    }

    /**
     * 长按时消费掉事件 并设置阴影
     *
     * @param v
     * @return
     */
    private View mDragView;

    @Override
    public boolean onLongClick(View v)
    {
        mDragView = v;
        if (v.getParent() == mShowGrid)
        {
            DragShadowBuilder builder = new DragShadowBuilder(v);
            v.startDrag(null, builder, null, 0);
            v.setBackgroundResource(R.drawable.shape_dialog_disable);
            return true;
        }
        return false;
    }

    /**
     * @param v
     * @param event
     * @return 返回true 表示处理ACTION_DRAG_STARTED 之外的所有的事件消费
     */
    @Override
    public boolean onDrag(View v, DragEvent event)
    {
        switch (event.getAction())
        {
            case DragEvent.ACTION_DRAG_STARTED: //开始拖拽
                //初始化每一个小方格条目 保存;
                initRect();
                break;
            case DragEvent.ACTION_DRAG_ENTERED: //entered 与exited 对应 进入GridLayout范围
                break;
            case DragEvent.ACTION_DRAG_LOCATION://相当与move
                //获取x y坐标 查看坐标在哪个矩阵里面(用一个矩阵的数组 记录了所有的小方格)
                int index = getRectIndex((int) event.getX(), (int) event.getY());
                if (index > -1) //-1表示没有找到对应的区间 或者在自己区间内部移动 没有移动到别的矩阵区间
                {
                    //在longClick的时候 需要记录住拖拽的哪个view
                    mShowGrid.removeView(mDragView);
                    mShowGrid.addView(mDragView, index);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:  //退出了GridLayout的范围
                break;
            case DragEvent.ACTION_DROP:         //放手准备落下
                break;
            case DragEvent.ACTION_DRAG_ENDED:   //结束
                mDragView.setBackgroundResource(R.drawable.shape_dialog_enable);
                break;
        }
        return true;
    }

    /**
     * 通过X,Y找到在哪个区间
     *
     * @param x
     * @param y
     * @return
     */
    private Rect mPrevRect = new Rect();    //初始化一个空的 保存住拖拽后的那个

    private int getRectIndex(int x, int y)
    {
        if (!mPrevRect.contains(x, y))      //不包含时执行,优化拖拽时 没有离开自己的原始区间时 疯狂执行增加删除操作
        {
            for (int i = 0; i < mRects.length; i++)
            {                                   //注意,拖拽某个矩阵时,系统默认取其中心点为x,y的坐标,不是左上点
                if (mRects[i].contains(x, y))   //如果x ,y在某个矩阵的区间 则表示移动到了对应的区间
                {
                    mPrevRect = mRects[i];
                    return i;
                }
            }
        }
        return -1;
    }

    //当start Drag当下 用一个矩阵数组保存下所有的当前的小矩阵的坐标
    private Rect[] mRects;

    private void initRect()
    {
        mRects = new Rect[mShowGrid.getChildCount()];
        for (int i = 0; i < mShowGrid.getChildCount(); i++)
        {
            View child = mShowGrid.getChildAt(i);
            //通过左上右下 圈住一块矩阵的区间 当move的时候 拿到 x, y的坐标值 匹配是否被包含在某个矩阵中
            mRects[i] = new Rect(child.getLeft(), child.getTop()
                    , child.getRight(), child.getBottom());
        }
    }
}
