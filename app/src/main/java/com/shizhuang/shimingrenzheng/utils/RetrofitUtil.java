package com.shizhuang.shimingrenzheng.utils;

import android.util.Log;

import com.shizhuang.shimingrenzheng.http.Api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 类用途:
 * 作者:shizhuangzhuang
 * 时间: 2017/7/3 21:39.
 */

public class RetrofitUtil {

    private static Api api;
    public static final String TOKEN = "a346554887c5bb8e2b05babe9d6eea45";
    private RetrofitUtil() {
        throw new AssertionError();
    }

    private static final String HOST = "http://scpmm.com/";

    /**
     * 初始化网络通信服务
     */
    public static void init() {
        // 指定缓存路径,缓存大小100Mb
/*        Cache cache = new Cache(new File(AndroidApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);*/
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//.cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(sLoggingInterceptor)
//                .addInterceptor(sRewriteCacheControlInterceptor)
//                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(HOST)
                .build();
        api = retrofit.create(Api.class);
    }

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                Log.d("LogTAG", "request.body() == null");
            }
            //打印url信息
            final Response response = chain.proceed(request);

            return response;
        }
    };

    private static String parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }
    /************************************ API *******************************************/
    /**
     * 上传个人信息
     *  post
     *
     */
    public static void upLoadingImage(String user_name, String user_card, String gesture_img, String positive_img, String reverse_img, Observer<ResponseBody> subscriber) {
        api.uploadImage(TOKEN, user_name, user_card, gesture_img, positive_img, reverse_img)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
