package com.gitee.moon.internal

import com.gitee.moon.GlobalNetworkResultInterceptor
import com.gitee.moon.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MoonCallAdapterFactory private constructor(
    private val interceptor: GlobalNetworkResultInterceptor = GlobalNetworkResultInterceptor.Default
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): MoonCallAdapter? =
        when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(callType)) {
                    NetworkResult::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        MoonCallAdapter(resultType, interceptor)
                    }
                    else -> null
                }
            }
        else -> null
        }

    companion object {

        fun create(
            interceptor: GlobalNetworkResultInterceptor = GlobalNetworkResultInterceptor.Default
        ): MoonCallAdapterFactory = MoonCallAdapterFactory(interceptor)

    }

    class MoonCallAdapter constructor(
        private val resultType: Type,
        private val interceptor: GlobalNetworkResultInterceptor
    ) : CallAdapter<Type, Call<NetworkResult<Type>>> {

        override fun responseType() = resultType

        override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> =
            MoonResponseCallDelegate(call, interceptor)

    }

}