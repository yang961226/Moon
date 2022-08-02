package com.gitee.moon.internal

import com.gitee.moon.GlobalNetworkResultInterceptor
import com.gitee.moon.NetworkResult
import com.gitee.moon.ktx.toExceptionResult
import com.gitee.moon.ktx.toNetworkResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class MoonResponseCallDelegate<T>(
    private val proxyCall: Call<T>,
    private val interceptor: GlobalNetworkResultInterceptor
) :
    Call<NetworkResult<T>> {

    override fun enqueue(callback: Callback<NetworkResult<T>>) =
        proxyCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@MoonResponseCallDelegate,
                    Response.success(
                        response.toNetworkResult(interceptor)
                    )
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@MoonResponseCallDelegate,
                    Response.success(t.toExceptionResult())
                )
            }

        })

    override fun isExecuted(): Boolean = proxyCall.isExecuted

    override fun cancel() = proxyCall.cancel()

    override fun isCanceled(): Boolean = proxyCall.isCanceled

    override fun request(): Request = proxyCall.request()

    override fun timeout(): Timeout = proxyCall.timeout()

    override fun clone(): Call<NetworkResult<T>> =
        MoonResponseCallDelegate(proxyCall.clone(), interceptor)

    override fun execute(): Response<NetworkResult<T>> = throw NotImplementedError()


}