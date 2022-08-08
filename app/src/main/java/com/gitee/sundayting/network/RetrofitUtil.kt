package com.gitee.sundayting.network

import com.gitee.sundayting.moon.GlobalNetworkResultInterceptor
import com.gitee.sundayting.moon.NetworkResult
import com.gitee.sundayting.moon.internal.MoonCallAdapterFactory
import com.gitee.sundayting.moon.isSuccess
import com.gitee.sundayting.moon.ktx.toException
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(MoonCallAdapterFactory.create(WanGlobalNetworkResultInterceptor))
            .baseUrl("https://www.wanandroid.com")
            .build()
    }

    object WanGlobalNetworkResultInterceptor : GlobalNetworkResultInterceptor {

        override fun <T> onIntercept(networkResult: NetworkResult<T>): NetworkResult<T> {
            return if (networkResult.isSuccess() && networkResult.body is WanBeanWrapper<*>) {
                //类型强转
                val wanBeanWrapper = (networkResult.body as WanBeanWrapper<*>)
                //如果不成功
                if (!wanBeanWrapper.isSuccessful()) {
                    return WanErrorException(wanBeanWrapper.errorMsg).toException()
                }
                networkResult
            } else {
                networkResult
            }
        }

    }

}