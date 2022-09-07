package com.gitee.sundayting.network

import android.util.Log
import com.gitee.sundayting.moon.MoonCallAdapterFactory
import com.gitee.sundayting.moon.NResult
import com.gitee.sundayting.moon.global.NetworkResultTransformer
import com.gitee.sundayting.moon.ktx.isNSuccess
import com.gitee.sundayting.moon.ktx.toNFailure
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
            .addCallAdapterFactory(MoonCallAdapterFactory.create(WanNetworkResultTransformer))
            .baseUrl("https://www.wanandroid.com")
            .build()
    }

    object WanNetworkResultTransformer : NetworkResultTransformer {

        override fun <T> transformerBy(result: NResult<T>): NResult<T> {
            return if (result.isNSuccess() && result.body is WanBeanWrapper<*>) {
                val wanBeanWrapper = (result.body as WanBeanWrapper<*>)
                if (!wanBeanWrapper.isSuccessful()) {
                    return WanErrorException(wanBeanWrapper.errorMsg).toNFailure()
                }
                result
            } else {
                result
            }
        }

    }

}