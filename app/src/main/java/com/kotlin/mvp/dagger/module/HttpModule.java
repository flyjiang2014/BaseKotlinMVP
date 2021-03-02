package com.kotlin.mvp.dagger.module;
import android.text.TextUtils;
import com.kotlin.mvp.BuildConfig;
import com.kotlin.mvp.app.App;
import com.kotlin.mvp.app.Constants;
import com.kotlin.mvp.dagger.qualifier.FileUpUrl;
import com.kotlin.mvp.dagger.qualifier.MyUrl;
import com.kotlin.mvp.http.api.Apis;
import com.kotlin.mvp.http.api.FileUpApis;
import com.kotlin.mvp.http.cookie.CookieJarImpl;
import com.kotlin.mvp.http.cookie.store.DBCookieStore;
import com.kotlin.mvp.http.interceptor.HttpLoggingInterceptor;
import com.kotlin.mvp.http.utils.HttpsUtils;
import com.kotlin.mvp.utils.SharepreferenceUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.FormBody;

/**
 * Created by flyjiang on 2019/8/2.
 */

@Module
public class HttpModule {

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }


    @Singleton
    @Provides
    @MyUrl
    Retrofit provideMyRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, Constants.INSTANCE.getHOST());
    }

    @Singleton
    @Provides
    @FileUpUrl
    Retrofit provideFileUpRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, Constants.INSTANCE.getHOST());
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder) {

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
         builder.cookieJar(new CookieJarImpl(new DBCookieStore(App.instance))); //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失
        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        // HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        HttpsUtils.SSLParams sslParams3 = null;
        try {
            sslParams3 = HttpsUtils.getSslSocketFactory(App.instance.getAssets().open("cb.crt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams3.sSLSocketFactory, sslParams3.trustManager);
        Interceptor apiKey = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder().header("User-Agent", "deyingSoft/" + BuildConfig.VERSION_NAME + "(userclient;Android" + android.os.Build.VERSION.RELEASE + ")").build();
                return chain.proceed(addParam(request));
            }
        };
        builder.addInterceptor(apiKey); //   设置统一的请求头部参数
        //设置超时
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("FLY_LOG");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }


    @Singleton
    @Provides
    Apis provideMyService(@MyUrl Retrofit retrofit) {
        return retrofit.create(Apis.class);
    }

    @Singleton
    @Provides
    FileUpApis provideFileUpService(@FileUpUrl Retrofit retrofit) {
        return retrofit.create(FileUpApis.class);
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 添加公共参数
     */
    private Request addParam(Request oldRequest) {
        Request newRequest;

        if (oldRequest.method().equals("GET")) {
            newRequest = getRequestParams(oldRequest);
        } else {  //POST请求
            newRequest = postRequestParams(oldRequest);
        }
        return newRequest;
    }

    /**
     * get请求公共参数
     */
    private Request getRequestParams(Request request) {
        HttpUrl.Builder builder = request.url()
                .newBuilder()
                .setEncodedQueryParameter("token", !TextUtils.isEmpty(SharepreferenceUtil.getString(Constants.TOKEN)) ? SharepreferenceUtil.getString(Constants.TOKEN) : "")
                .setEncodedQueryParameter("deviceNum", SharepreferenceUtil.getString(Constants.KDD_ANDROID_ID));
        return request.newBuilder()
                .method(request.method(), request.body())
                .url(builder.build())
                .build();
    }


    /**
     * post请求公共参数
     */
    private Request postRequestParams(Request request) {
        if (request.body() instanceof FormBody) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();
            //添加原请求体
            for (int i = 0; i < formBody.size(); i++) {
                bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
            }
            formBody = bodyBuilder.addEncoded("token", SharepreferenceUtil.getString(Constants.TOKEN))
                    .addEncoded("deviceNum", SharepreferenceUtil.getString(Constants.KDD_ANDROID_ID))
                    .build();
            request = request.newBuilder().post(formBody).build();
        } else if (request.body() instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) request.body();
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            //添加原请求体
            for (int i = 0; i < multipartBody.size(); i++) {
                builder.addPart(multipartBody.part(i));
            }
            builder.addFormDataPart("token", SharepreferenceUtil.getString(Constants.TOKEN))
                    .addFormDataPart("deviceNum", SharepreferenceUtil.getString(Constants.KDD_ANDROID_ID));
            multipartBody = builder.build();
            request = request.newBuilder().post(multipartBody).build();
        } else {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = bodyBuilder.addEncoded("token", SharepreferenceUtil.getString(Constants.TOKEN))
                    .addEncoded("deviceNum", SharepreferenceUtil.getString(Constants.KDD_ANDROID_ID))
                    .build();
            request = request.newBuilder().post(formBody).build();
        }
        return request;
    }
}
