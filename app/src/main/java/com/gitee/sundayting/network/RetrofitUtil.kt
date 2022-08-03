package com.gitee.sundayting.network

import com.gitee.moon.GlobalNetworkResultInterceptor
import com.gitee.moon.NetworkResult
import com.gitee.moon.internal.MoonCallAdapterFactory
import com.gitee.moon.ktx.toExceptionResult
import okhttp3.OkHttpClient
import okio.IOException
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
            return if (
            //只有成功才转换
                networkResult is NetworkResult.Success &&
                //只转换这种类型的Bean
                networkResult.responseBody is WanBeanWrapper<*>
            ) {
                //类型强转
                val wanBeanWrapper = (networkResult.responseBody as WanBeanWrapper<*>)
                //如果不成功
                if (!wanBeanWrapper.isSuccessful()) {
                    return IOException(wanBeanWrapper.errorMsg).toExceptionResult()
                }
                networkResult
            } else {
                networkResult
            }
        }

    }

}