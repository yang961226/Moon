package com.gitee.sundayting.network

import com.gitee.sundayting.moon.NetworkResultTransformer
import com.gitee.sundayting.moon.Result
import com.gitee.sundayting.moon.internal.MoonCallAdapterFactory
import com.gitee.sundayting.moon.isSuccess
import com.gitee.sundayting.moon.ktx.toExceptionResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    val instance: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(MoonCallAdapterFactory.create(WanNetworkResultTransformer))
            .baseUrl("https://www.wanandroid.com")
            .build()
    }

    object WanNetworkResultTransformer : NetworkResultTransformer {

        override fun <T> transformerBy(result: Result<T>): Result<T> {
            return if (result.isSuccess() && result.body is WanBeanWrapper<*>) {
                val wanBeanWrapper = (result.body as WanBeanWrapper<*>)
                if (!wanBeanWrapper.isSuccessful()) {
                    return WanErrorException(wanBeanWrapper.errorMsg).toExceptionResult()
                }
                result
            } else {
                result
            }
        }

    }

}