package com.jc.packagepndemo.api;

import com.jc.packagepndemo.model.AliAddrsBean;
import com.jc.packagepndemo.model.IndexRequestBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by solar on 2016/8/10.
 */
public interface ApiService {
    //http://gc.ditu.aliyun.com/geocoding?a=上海市&aa=松江区&aaa=车墩镇

    //Retrofit建议url以/结束，注解不要以/开始

    @GET("http://gc.ditu.aliyun.com/geocoding?a=上海市&aa=松江区&aaa=车墩镇")
    Call<AliAddrsBean> getIndexContentOne();

 //1.这部分是用“http://gc.ditu.aliyun.com/geocoding/” 请求的
    //注解中必须要有一部分的url地址，不能光是请求体
    @GET("a=上海市&aa=松江区&aaa=车墩镇")
    Call<AliAddrsBean> getIndexContentTwo();

 //2.这部分使用 “http://gc.ditu.aliyun.com” 请求的
    @GET("geocoding?a=上海市&aa=松江区&aaa=车墩镇")
    Call<AliAddrsBean> getIndexContentThree();

    @GET("{parameters}?a=苏州市")
    Call<AliAddrsBean> getIndexContentFour(
            @Path("parameters") String parameters);

    //取代块只能取代url，不能取代参数，且@path的作用就是专职于取代块调用的时候把参数传进来
    @GET("{parameters}?{parameter2}=苏州市")
    Call<AliAddrsBean> getIndexContentFour(
            @Path("parameters") String parameters,@Path("parameters") String parameter2);

    @GET("{parameters}?a={city1}")
    Call<AliAddrsBean> getIndexContentFive(
                    @Path("parameters") String parameters,@Path("city1") String city1);

    @GET("geocoding?")
    Call<AliAddrsBean> getIndexContentSix(
            @Query("a") String key1,
            @Query("aa") String key2,
            @Query("aaa") String key3
    );

    @POST("geocoding?")
    Call<AliAddrsBean> getIndexContentSeven(
            @Query("a") String key1,
            @Query("aa") String key2,
            @Query("aaa") String key3
    );

   @GET("geocoding?")
   Call<AliAddrsBean> getIndexContentEight(
           @QueryMap Map<String, Object> map
           );

   @POST("geocoding?")
   Call<AliAddrsBean> getIndexContentNine(
           @Body IndexRequestBean indexRequestBean);

   @FormUrlEncoded
   @POST("geocoding?")
   Call<AliAddrsBean> getIndexContentTen(
           @Field("a") String city,
           @Field("aa") String citys,
           @Field("aaa") String cityss
   );


   //发送字节流数据
   @Multipart
   @POST("geocoding?")
   Call<AliAddrsBean> getIndexContentTen1(
           @Part("a") RequestBody city,
           @Part("aa") RequestBody citya,
           @Part("aaa") RequestBody cityaa);

   @Headers({"key:web_service_key","web_vsersion:1.01","app_version:1.02"})
   @GET("geocoding?")
   Call<AliAddrsBean> getIndexContentTen2(
           @Query("a") String city
   );


   //我们适配Rxjava的时候，只需要将返回结果的call变成
   @GET("geocoding?")
   Observable<AliAddrsBean> getIndexContentEleven(
           @Query("a") String city);



}
