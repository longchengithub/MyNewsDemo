package com.mydemo.mynewsdemo.http;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.mydemo.mynewsdemo.NewsApp;
import com.mydemo.mynewsdemo.http.api.ApiConstant;
import com.mydemo.mynewsdemo.http.api.CategoriesService;
import com.mydemo.mynewsdemo.http.api.NewsPagerService;
import com.mydemo.mynewsdemo.utils.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenlong on 2016/12/19.
 * Retrofit的Helper类 封装好okHttpClient 与 RxJava
 */

public class RetrofitHelper
{
    private static OkHttpClient mOkHttpClient;

    static
    {
        initOkHttpClient();
    }

    private static void initOkHttpClient()
    {
        if (mOkHttpClient == null)
        {
            synchronized (RetrofitHelper.class)
            {
                if (mOkHttpClient == null)
                {
                    mOkHttpClient = new OkHttpClient.Builder()
//                            .cache(getCache())                                     //设置缓存
//                            .addInterceptor(getLog())                              //添加日志管理
//                            .addNetworkInterceptor(new CacheInterceptor())         //添加缓存的配置
                            .addNetworkInterceptor(new StethoInterceptor())        //添加拦截器
                            .retryOnConnectionFailure(true)                        //设置自动重连
                            .connectTimeout(30, TimeUnit.SECONDS)                  //30秒连接超时
                            .writeTimeout(20, TimeUnit.SECONDS)                    //20秒写入超时
                            .readTimeout(20, TimeUnit.SECONDS)                     //同上
                            .build();
                }
            }
        }
    }

    /**
     * 返回分类的json字符串
     *
     * @return
     */
    public static CategoriesService getCategoriesService()
    {
        return serviceImpl(CategoriesService.class, ApiConstant.BASE_URL);
    }

    public static NewsPagerService getNewsPagersService()
    {
        return serviceImpl(NewsPagerService.class, ApiConstant.BASE_URL);
    }

    /**
     * 对OkHttpClient的Retrofit封装  泛型的方法的封装
     *
     * @param clazz 传入一个接口的Class 返回这个Class
     * @param url   连接地址
     * @param <T>
     * @return
     */
    private static <T> T serviceImpl(Class<T> clazz, String url)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)                       //联网地址
                .client(mOkHttpClient)              //添加okHttpClient
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //添加适配器
                .addConverterFactory(GsonConverterFactory.create())         //添加Gson转换
                .build();
        return retrofit.create(clazz);
    }

    /**
     * 设置缓存路径和大小
     *
     * @return
     */
    private static Cache getCache()
    {
        Cache cache = new Cache(new File(NewsApp.getInstance().getCacheDir(),
                "newsCache"), 1024 * 1024 * 10);
        return cache;
    }

    /**
     * 设置日志管理拦截器
     *
     * @return
     */
    private static HttpLoggingInterceptor getLog()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    private static class CacheInterceptor implements Interceptor
    {

        @Override
        public Response intercept(Chain chain) throws IOException
        {

            // 有网络时 设置缓存超时时间1个小时
            int maxAge = 60 * 60;
            // 无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();          //请求的
            if (CommonUtil.isNetworkAvailable(NewsApp.getInstance()))
            {
                //有网络时只从网络获取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else
            {
                //无网络时只从缓存中读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request); //响应的
            if (CommonUtil.isNetworkAvailable(NewsApp.getInstance()))
            {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else
            {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }
}
