package com.yushilei.qiushibaike3.interfaces;

import com.yushilei.qiushibaike3.entitys.CommentsResponse;
import com.yushilei.qiushibaike3.entitys.ZhuangxiangResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by yushilei on 2015/12/29.
 */
public interface ZhuanxiangService {
    @GET("article/list/{type}")
    Call<ZhuangxiangResponse> getResponse(@Path("type") String type, @Query("page") int page);

    @GET("article/{id}/comments")
    Call<CommentsResponse> getResponse(@Path("id") int id, @Query("page") int page);
}
