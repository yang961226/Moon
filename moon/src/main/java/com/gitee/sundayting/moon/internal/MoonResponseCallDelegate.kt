package com.gitee.sundayting.moon.internal

import com.gitee.sundayting.moon.MoonInitializer
import com.gitee.sundayting.moon.NResult
import com.gitee.sundayting.moon.ktx.toNFailure
import com.gitee.sundayting.moon.ktx.toNetResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class MoonResponseCallDelegate<T>(
    private val realCall: Call<T>,
) : Call<NResult<T>> {

    override fun enqueue(callback: Callback<NResult<T>>) =
        realCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@MoonResponseCallDelegate,
                    Response.success(
                        MoonInitializer.instance.resultTransformer.transformerBy(
                            response.toNetResult()
                        )
                    )
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@MoonResponseCallDelegate,
                    Response.success(t.toNFailure())
                )
            }

        })

    override fun isExecuted(): Boolean = realCall.isExecuted

    override fun cancel() = realCall.cancel()

    override fun isCanceled(): Boolean = realCall.isCanceled

    override fun request(): Request = realCall.request()

    override fun timeout(): Timeout = realCall.timeout()

    override fun clone(): Call<NResult<T>> =
        MoonResponseCallDelegate(realCall.clone())

    override fun execute(): Response<NResult<T>> = throw NotImplementedError()


}