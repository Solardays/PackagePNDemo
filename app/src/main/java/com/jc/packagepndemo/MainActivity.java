package com.jc.packagepndemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jc.packagepndemo.api.ApiService;
import com.jc.packagepndemo.api.GithubService;
import com.jc.packagepndemo.model.AliAddrsBean;
import com.jc.packagepndemo.model.ContributorBean;
import com.jc.packagepndemo.model.IndexRequestBean;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobclickAgent.setDebugMode(true);
        MobclickAgent.onProfileSignIn("userID");

//        Logset();
//
//        initPackpn();

//        testRetrofit();
        //http://gc.ditu.aliyun.com/geocoding?a=上海市&aa=松江区&aaa=车墩镇
//        testRetrofit2("http://gc.ditu.aliyun.com/geocoding/");//Retrofit建议url以/结束，注解不要以/开始
        testRetrofit2("http://gc.ditu.aliyun.com");//


    }

    private void testRetrofit() {


        String API = "https://api.github.com";

        //https://api.github.com/repos/square/retrofit/contributors
//        https://api.github.com/users/list?sort=desc
        //http://ip.taobao.com/service/getIpInfo.php/ip=63.223.108.42

        //创建Retrofit对象
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GithubService service = retrofit.create(GithubService.class);

        service.getContributors("square", "retrofit").enqueue(
                new Callback<List<ContributorBean>>() {
                    @Override
                    public void onResponse(Call<List<ContributorBean>> call
                            , Response<List<ContributorBean>> response) {
                        Log.e("MainActivity", response.body().toString());
                        for (ContributorBean con : response.body()) {
                            Log.e("MainActivity", con.login);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ContributorBean>> call
                            , Throwable t) {
                        t.printStackTrace();
                    }
                });

        /*
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });
       */
    }

    public void testRetrofit2(String url) {
        //每一个Call实例可以同步(call.excute())或者异步(call.enquene(CallBack<!--?--> callBack))的被执行，
        //每一个实例仅仅能够被使用一次，但是可以通过clone()函数创建一个新的可用的实例。
        //默认情况下，Retrofit只能够反序列化Http体为OkHttp的ResponseBody类型
        //并且只能够接受ResponseBody类型的参数作为@body
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();
        ApiService service = retrofit.create(ApiService.class);

//        Call requestInde = service.getIndexContentTwo();
//        Call requestInde = service.getIndexContentFour("geocoding","a");
//        Call requestInde = service.getIndexContentFive("geocoding","苏州市");

//        Call requestInde = service.getIndexContentSix("上海市","松江区","车墩镇");
//        Call requestInde = service.getIndexContentSeven("上海市","松江区","车墩镇");

//        Map map = new HashMap();
//        map.put("a", "上海市");
//        map.put("aa", "黄浦区");
//        Call requestInde = service.getIndexContentEight(map);

        IndexRequestBean indexRequestBean = new IndexRequestBean();
        indexRequestBean.a="上海市";
        indexRequestBean.aa="松江区";
        indexRequestBean.aaa="车墩镇";
        Call requestInde = service.getIndexContentNine(indexRequestBean);
        requestInde.enqueue(new Callback<AliAddrsBean>(){
            @Override
            public void onResponse(Call<AliAddrsBean> call, Response<AliAddrsBean> response) {
                Log.e("MainActivity", response.body().lon+"");
            }

            @Override
            public void onFailure(Call<AliAddrsBean> call, Throwable t) {

            }
        });

        //适配Rxjava
        Observable requestInde2 = service.getIndexContentEleven("苏州市");
        requestInde2.subscribeOn(Schedulers.newThread())//这里需要注意的是，网络请求在非ui线程。如果返回结果是依赖于Rxjava的，则需要变换线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AliAddrsBean>() {
                    @Override
                    public void call(AliAddrsBean o) {
                        Log.e("MainActivity", "test:"+o.lon+"");
                    }
                });

    }


    private void Logset() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        Log.e("MainActivity", DEVICE_ID);

        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String MAC_ID = wm.getConnectionInfo().getMacAddress();
        Log.e("MainActivity", MAC_ID);

        getDeviceInfo(this);

        Log.e("MainActivity", getDeviceInfo(this));


    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        MobclickAgent.onProfileSignOff();
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e("MainActivity", "checkPermission:Build.VERSION.SDK_INT >= 23");
            try {
                Class clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            Log.e("MainActivity", "checkPermission:Build.VERSION.SDK_INT < 23");
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int checkSelfPermission(String permission) {
        Log.e("MainActivity", "checkSelfPermission:" + permission);
        return super.checkSelfPermission(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("MainActivity", "onRequestPermissionsResult:");
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
        }
    }

    public boolean checkPerMission(String permission) {
        Log.e("MainActivity", "checkPerMission:" + permission);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        return false;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            String device_id = null;

            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                device_id = tm.getDeviceId();
                Log.e("MainActivity", "device_id:" + device_id);
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                    Log.e("MainActivity", "mac:" + mac);
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            Log.e("MainActivity", "ANDROID_ID:" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID));
            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void jump2(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }

}
