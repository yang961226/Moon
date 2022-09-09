package com.gitee.sundayting.network

import android.util.Log
import com.gitee.sundayting.moon.MoonCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    val instance: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor { message -> Log.d("网络请求日志", message) }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(MoonCallAdapterFactory.create())
            .baseUrl("https://www.wanandroid.com")
            .build()
    }

}