package com.gitee.sundayting.moon.internal

import com.gitee.sundayting.moon.NetworkResultTransformer
import com.gitee.sundayting.moon.Result
import com.gitee.sundayting.moon.ktx.toExceptionResult
import com.gitee.sundayting.moon.ktx.toResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class MoonResponseCallDelegate<T>(
    private val realCall: Call<T>,
    private val transformer: NetworkResultTransformer
) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) =
        realCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@MoonResponseCallDelegate,
                    Response.success(
                        response.toResult {
                            transformer.transformerBy(it)
                        }
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

    override fun isExecuted(): Boolean = realCall.isExecuted

    override fun cancel() = realCall.cancel()

    override fun isCanceled(): Boolean = realCall.isCanceled

    override fun request(): Request = realCall.request()

    override fun timeout(): Timeout = realCall.timeout()

    override fun clone(): Call<Result<T>> =
        MoonResponseCallDelegate(realCall.clone(), transformer)

    override fun execute(): Response<Result<T>> = throw NotImplementedError()


}