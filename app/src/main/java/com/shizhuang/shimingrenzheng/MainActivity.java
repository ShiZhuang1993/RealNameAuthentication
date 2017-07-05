package com.shizhuang.shimingrenzheng;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liji.takephoto.TakePhoto;
import com.shizhuang.shimingrenzheng.http.RetrofitUtils;
import com.shizhuang.shimingrenzheng.utils.IdCardUtil;
import com.shizhuang.shimingrenzheng.utils.RegexUtil;
import com.shizhuang.shimingrenzheng.utils.RetrofitUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_main_name;
    private EditText ed_main_phone;
    private EditText ed_main_id;
    private ImageView image_main_renmian;
    private ImageView image_main_guohui;
    private ImageView image_main_shouchizhao;
    private Button bt_mian_wancheng;
    private String name;
    private String id;
    private String path1;
    private String path2;
    private String path3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mains);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initView();
    }

    private void initView() {
        ed_main_name = (EditText) findViewById(R.id.ed_main_name);
        ed_main_phone = (EditText) findViewById(R.id.ed_main_phone);
        ed_main_id = (EditText) findViewById(R.id.ed_main_id);
        image_main_renmian = (ImageView) findViewById(R.id.image_main_renmian);
        image_main_guohui = (ImageView) findViewById(R.id.image_main_guohui);
        image_main_shouchizhao = (ImageView) findViewById(R.id.image_main_shouchizhao);
        bt_mian_wancheng = (Button) findViewById(R.id.bt_mian_wancheng);
        bt_mian_wancheng.setOnClickListener(this);
        image_main_renmian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto takePhoto = new TakePhoto(MainActivity.this);
                takePhoto.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {
                        Glide.with(MainActivity.this).load("file://" + path).into
                                (image_main_renmian);
                        path1=path;
                    }
                });
                takePhoto.show();
            }
        });
        image_main_guohui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto takePhoto = new TakePhoto(MainActivity.this);
                takePhoto.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {
                        Glide.with(MainActivity.this).load("file://" + path).into
                                (image_main_guohui);
                        path2=path;
                    }
                });
                takePhoto.show();
            }
        });
        image_main_shouchizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto takePhoto = new TakePhoto(MainActivity.this);
                takePhoto.setOnPictureSelected(new TakePhoto.onPictureSelected() {
                    @Override
                    public void select(String path) {
                        Glide.with(MainActivity.this).load("file://" + path).into
                                (image_main_shouchizhao);
                        path3=path;
                    }
                });
                takePhoto.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mian_wancheng:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        name = ed_main_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入您的真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        //手机号码判断
        String phone = ed_main_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入您的联系电话", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean mobile = checkMobile(phone);
        if (mobile == false) {
            Toast.makeText(this, "手机号码不合法", Toast.LENGTH_SHORT).show();
            return;

        }

        //身份证判断
        id = ed_main_id.getText().toString().trim();
        boolean realIDCard = RegexUtil.isRealIDCard(id);
        if (realIDCard == false) {
            IdCardUtil idCardUtil = new IdCardUtil(id);
            int correct = idCardUtil.isCorrect();
            String errMsg = idCardUtil.getErrMsg(correct);
            Toast.makeText(MainActivity.this, errMsg + "", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image_main_renmian.getDrawable() == null) {
            Toast.makeText(this, "请上传证件", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image_main_guohui.getDrawable() == null) {
            Toast.makeText(this, "请上传证件", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image_main_shouchizhao.getDrawable() == null) {
            Toast.makeText(this, "请上传证件", Toast.LENGTH_SHORT).show();
            return;
        }else{
            upLoad();
        }
    }

    //手机号码判断
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 上传图片
     * create by weiang at 2016/5/20 17:33.
     */
    private void upLoad() {
        RetrofitUtil.init();
        RetrofitUtil.upLoadingImage(name, id, path1, path2, path3, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
            //410726199505245417
            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                try {
                    Log.e("responseBody--------->",responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("responseBody--------->",e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
