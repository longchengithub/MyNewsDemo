package com.mydemo.mynewsdemo.http.api;

import com.mydemo.mynewsdemo.bean.CategoriesBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by chenlong on 2016/12/21.
 */

public interface CategoriesService
{
    @GET("categories.json")
    Observable<CategoriesBean> getCategories();
}
