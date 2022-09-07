package com.gitee.sundayting.moon

import com.gitee.sundayting.moon.NResult.NFailure
import com.gitee.sundayting.moon.NResult.NSuccess
import com.gitee.sundayting.moon.exception.NException
import okhttp3.Headers
import retrofit2.Response

/**
 * 网络请求结果
 *
 * 有以下子类：
 *
 * [NSuccess] 网络请求成功
 *
 * [NFailure] 网络请求失败
 *
 */
sealed class NResult<out T> private constructor() {

    /**
     * 网络请求成功
     */
    class NSuccess<T> internal constructor(private val response: Response<T>) : NResult<T>() {

        init {
            assert(response.body() != null)
            assert(response.isSuccessful)
        }

        val body by lazy { response.body()!! }
        val httpCode by lazy { response.code() }
        val httpMessage: String by lazy { response.message() }
        val rawResponse: okhttp3.Response by lazy { response.raw() }
        val headers: Headers by lazy { response.headers() }
    }

    /**
     * 网络请求失败
     */
    class NFailure internal constructor(val nException: NException) : NResult<Nothing>() {
        override fun toString(): String =
            "[${NFailure::class.simpleName}](message=${nException.message})"
    }

}
