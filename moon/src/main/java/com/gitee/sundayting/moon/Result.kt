package com.gitee.sundayting.moon

import okhttp3.Headers
import retrofit2.Response
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Result<out T> {

    interface ResponseContent {
        val code: Int
        val httpMessage: String
        val headers: Headers
    }

    class Success<T> internal constructor(response: Response<T>) : Result<T>(), ResponseContent {

        init {
            assert(response.body() != null)
            assert(response.isSuccessful)
        }

        val body by lazy { response.body()!! }
        override val code by lazy { response.code() }
        override val httpMessage: String by lazy { response.message() }
        override val headers: Headers by lazy { response.headers() }
    }

    class Failure internal constructor(val exception: Throwable) : Result<Nothing>()

    class ServerErrorException internal constructor(response: Response<*>) : Exception(),
        ResponseContent {
        init {
            assert(response.errorBody() != null)
        }

        val errorBody by lazy { response.errorBody()!!.string() }
        override val code by lazy { response.code() }
        override val httpMessage: String by lazy { response.message() }
        override val headers: Headers by lazy { response.headers() }
    }

}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success)
        returns(false) implies (this@isSuccess is Result.Failure)
    }
    return this is Result.Success
}

