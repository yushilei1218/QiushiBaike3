package com.yushilei.qiushibaike3.Utils;

import com.yushilei.qiushibaike3.entitys.CommentsResponse;
import com.yushilei.qiushibaike3.entitys.SuggestResponse;
import com.yushilei.qiushibaike3.entitys.ZhuangxiangResponse;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by yushilei on 2015/12/31.
 */
public class HttpUtils {
    public interface Service {
        @GET("article/list/{type}")
        Call<SuggestResponse> getArticle(@Path("type") String type, @Query("page") int page,@Query("count")int count);

        @GET("article/{id}/comments")
        Call<CommentsResponse> getComments(@Path("id") long id, @Query("page") int page);
    }

    private static Service service;

    static {
        service = new Retrofit.Builder().baseUrl("http://m2.qiushibaike.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(Service.class);
    }

    public static Service getService() {
        return service;
    }
}
