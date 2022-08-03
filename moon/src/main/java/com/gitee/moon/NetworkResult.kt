package com.gitee.moon

import okhttp3.Headers
import retrofit2.Response

sealed class NetworkResult<T> {

    private interface ResponseGetter {
        //http状态码
        val code: Int

        //响应头
        val headers: Headers

        //请求Url（部分场景用于判断）
        val url: String
    }

    /**
     * 网络请求成功
     */
    class Success<T>(private val response: Response<T>) : NetworkResult<T>(), ResponseGetter {
        val responseBody by lazy { response.body()!! }
        override val code by lazy { response.code() }
        override val headers: Headers by lazy { response.headers() }
        override val url by lazy { response.raw().request.url.toString() }
    }

    /**
     * 网络请求失败
     */
    sealed class Failure<T> : NetworkResult<T>() {
        /**
         * HTTP协议错误
         */
        data class ServerError<T>(private val response: Response<T>) : Failure<T>(),
            ResponseGetter {
            val responseErrorMessage: String by lazy { response.errorBody()?.string().orEmpty() }
            override val code by lazy { response.code() }
            override val headers: Headers by lazy { response.headers() }
            override val url by lazy { response.raw().request.url.toString() }
        }

        /**
         * 网络请求出现异常
         */
        data class Exception<T>(val exception: Throwable) : Failure<T>() {
//            val exceptionMessage: String by lazy {
//                HttpExceptionParserHolder.parse(exception)
//            }
        }

    }
}

/**
 * 网络请求成功时操作
 */
inline fun <reified T> NetworkResult<T>.ifSuccess(action: (NetworkResult.Success<T>) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(this)
    return this
}

/**
 * 网络请求中，服务器已响应但HTTP协议出现错误时（例如404，502）操作
 */
inline fun <reified T> NetworkResult<T>.ifServerError(action: (NetworkResult.Failure.ServerError<T>) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Failure.ServerError) action(this)
    return this
}

/**
 * 网络请求中，出现异常时（例如解析JSON异常，超时异常，连接网络失败异常等）操作
 */
inline fun <reified T> NetworkResult<T>.ifException(action: (NetworkResult.Failure.Exception<T>) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Failure.Exception) action(this)
    return this
}

/**
 * 网络请求失败（包括[ifServerError]和[ifException]两种情况）
 */
inline fun <reified T> NetworkResult<T>.ifFailure(action: (errorMsg: String) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Failure) {
        action(getFailureMessage())
    }
    return this
}

inline fun <reified T> NetworkResult.Failure<T>.getFailureMessage(): String {
    return when (this) {
        is NetworkResult.Failure.ServerError -> responseErrorMessage
        is NetworkResult.Failure.Exception -> this.exception.message.orEmpty()
    }
}