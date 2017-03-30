package com.mydemo.mynewsdemo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mydemo.mynewsdemo.R;
import com.mydemo.mynewsdemo.http.api.ApiConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by chenlong on 2016/12/24.
 */
public class NewsDetailActivity extends Activity
{
    @BindView(R.id.icon_back)
    ImageView mBackIcon;
    @BindView(R.id.icon_textsize)
    ImageView mTextSizeIcon;
    @BindView(R.id.icon_share)
    ImageView mShareIcon;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.web_progress)
    ProgressBar mProgress;
    private WebSettings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        ShareSDK.initSDK(this, "1a4502175bd94");

        //1.给WebView设置一个加载的路径
        String detail = getIntent().getStringExtra("detail");
        mWebView.loadUrl(ApiConstant.URL + detail);

        //2.给webView设置一个监听类(普通版本)
//        mWebView.setWebViewClient(mWebViewClient);
        //2.给WebView设置一个监听类(高级版本,取其1即可)
        mWebView.setWebChromeClient(mWebChromeClient);

        //3.获取设置器
        mSettings = mWebView.getSettings();
        //4.开启JavaScript支持
        mSettings.setJavaScriptEnabled(true);
        //5.设置默认字体大小
        mSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //6.设置支持缩放
        mSettings.setSupportZoom(true);
        //7.设置更多适配屏幕的选项
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
    }

    /**
     * 高级监听类
     */
    private WebChromeClient mWebChromeClient = new WebChromeClient()
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            super.onProgressChanged(view, newProgress);
            //时时刷新进度条 ,当加载完毕后 隐藏progress
            mProgress.setProgress(newProgress);
            if (newProgress == 100)
            {
                mProgress.setVisibility(View.GONE);
            }
        }
    };

    @OnClick({R.id.icon_back, R.id.icon_textsize, R.id.icon_share})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_textsize:
                alertTextSizeDialog();
                break;
            case R.id.icon_share:
                showShare();
                break;
        }
    }

    private String[] chooseItems = {"幼儿体", "较小", "正常", "较大", "老年体"};
    private int selectIndex = 2;

    /**
     * 弹出一个选择字体的对话框
     */
    private void alertTextSizeDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");


        builder.setSingleChoiceItems(chooseItems, selectIndex, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                selectIndex = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mSettings.setTextSize(WebSettings.TextSize.values()[selectIndex]);
            }
        });

        builder.setNegativeButton("取消", null);

        builder.create().show();
    }

    private void showShare()
    {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
