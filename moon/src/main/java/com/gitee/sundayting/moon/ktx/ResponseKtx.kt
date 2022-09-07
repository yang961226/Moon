package com.gitee.sundayting.moon.ktx

import com.gitee.sundayting.moon.MoonInitializer
import com.gitee.sundayting.moon.NResult
import com.gitee.sundayting.moon.exception.NException
import com.gitee.sundayting.moon.exception.ServerErrorException
import retrofit2.Response


/**
 * 将[Response]转译成[NResult]
 *
 * 转换规则如下：
 *
 * 如果网络请求成功，即HTTP协议码处于200-300之间，则返回[NResult.NSuccess]
 *
 * 如果网络请求失败或者发生其他异常，则返回[NResult.NFailure]
 */
fun <T> Response<T>.toNetResult(): NResult<T> = try {
    if (isSuccessful) {
        toNSuccess()
    } else {
        toNFailure()
    }
} catch (t: Throwable) {
    t.toNFailure()
}

/**
 * 将[Response]转译为[NResult.NSuccess]
 */
fun <T> Response<T>.toNSuccess(): NResult.NSuccess<T> {
    assert(isSuccessful) { "严禁将失败的http请求转成成功结果！" }
    return NResult.NSuccess(this)
}

/**
 * 将[Response]转译为[NResult.NFailure]，这种情况只发生于HTTP协议错误的情况下
 *
 * 其中，[NResult.NFailure]的异常会通过[NException]包裹，其异常信息会用
 */
fun Response<*>.toNFailure(): NResult.NFailure {
    assert(!isSuccessful) { "严禁将成功的http请求转成失败结果！" }
    val serverException = ServerErrorException(code(), errorBody())
    return NResult.NFailure(
        NException(
            MoonInitializer.instance.nativeLangTransformer.rawToNativeMsg(serverException),
            serverException
        )
    )
}

fun Throwable.toNFailure(): NResult.NFailure {
    return NResult.NFailure(
        NException(
            MoonInitializer.instance.nativeLangTransformer.rawToNativeMsg(this),
            this
        )
    )
}
