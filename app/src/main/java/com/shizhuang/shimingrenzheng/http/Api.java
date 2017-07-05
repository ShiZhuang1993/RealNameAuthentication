package com.shizhuang.shimingrenzheng.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * 类用途：
 * 作者：ShiZhuangZhuang
 * 时间：2017/7/3 16:57
 */

public interface Api {
    @FormUrlEncoded
    @POST("index.php?r=agent-user/file")
    Observable<ResponseBody> uploadImage(@Field("token") String token,
                                         @Field("user_name") String name,
                                         @Field("user_card") String card,
                                         @Field("gesture_img") String imgs1,
                                         @Field("positive_img") String imgs2,
                                         @Field("reverse_img") String imgs3);

}
