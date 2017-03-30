package com.mydemo.mynewsdemo.http.api;

import com.mydemo.mynewsdemo.bean.NewsPagerBean;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by chenlong on 2016/12/22.
 */

public interface NewsPagerService
{
    @GET
    Observable<NewsPagerBean> getNewsPagers(@Url String path);
}
