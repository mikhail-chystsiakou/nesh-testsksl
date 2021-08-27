package com.nesh.mors.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitMp3JuicesService {
    @GET("/")
    Call<ResponseBody> getRequest();

    @Headers({
        "Referer: https://www.mp3juices.cc/",
        "Accept: text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01",
        "Accept-Encoding: gzip, deflate, br",
        "Accept-Language: en-GB,en;q=0.9,ru-RU;q=0.8,ru;q=0.7,en-US;q=0.6",
        "Connection: keep-alive",
        "X-Requested-With: XMLHttpRequest"
    })
    @GET("search.php")
    Call<ResponseBody> listSongs(
        @Header("Cookie") String cookie,
        @Query("callback") String callback,
        @Query("q") String q,
        @Query("k") String k,
        @Query("_") String _
    );

    @Headers({
        "Referer: https://www.mp3juices.cc/",
        "Accept: */*",
        "Accept-Encoding: gzip, deflate, br",
        "Accept-Language: en-GB,en;q=0.9,ru-RU;q=0.8,ru;q=0.7,en-US;q=0.6",
        "Connection: keep-alive",
        "X-Requested-With: XMLHttpRequest"
    })
    @POST("sources.php")
    Call<ResponseBody> enableSource(
        @Header("Cookie") String cookie,
        @Query("s") String source,
        @Query("p") String enable
    );

    @Headers({
        "Referer: https://www.mp3juices.cc/",
        "Accept: text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01",
//        "Accept-Encoding: gzip, deflate, br",
        "Accept-Language: en-GB,en;q=0.9,ru-RU;q=0.8,ru;q=0.7,en-US;q=0.6",
        "Connection: keep-alive",
        "X-Requested-With: XMLHttpRequest"
    })
    @GET("https://x.eex.cx/check.php")
    Call<ResponseBody> loadSong(
        @Header("Cookie") String cookie,
        @Query("callback") String callback,
        @Query("v") String v,
        @Query("f") String f,
        @Query("k") String k,
        @Query("t") String t,
        @Query("_") String _
    );
}
