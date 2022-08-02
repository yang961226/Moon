package com.gitee.moon.internal

import com.gitee.moon.GlobalNetworkResultInterceptor
import com.gitee.moon.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class MoonCallAdapter constructor(
    private val resultType: Type,
    private val interceptor: GlobalNetworkResultInterceptor
) : CallAdapter<Type, Call<NetworkResult<Type>>> {
    override fun responseType() = resultType

    override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> =
        MoonResponseCallDelegate(call, interceptor)

}