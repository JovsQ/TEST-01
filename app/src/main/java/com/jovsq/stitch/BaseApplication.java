package com.jovsq.stitch;

import android.app.Application;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by USER on 3/22/2018.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /** initialize okhttp client */
        File cacheDir = getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = getCacheDir();
        }
        final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        /** initialize ok http client */
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request request = chain.request();
                        return chain.proceed(request);
                    }
                })
                .cache(cache)
                .build();

        /** initialize picasso */
        final PicassoSingleton picassoSingleton = PicassoSingleton.getInstance();
        picassoSingleton.initPicasso(this, okHttpClient);
    }
}
