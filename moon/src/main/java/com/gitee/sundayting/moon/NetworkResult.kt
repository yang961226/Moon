package com.gitee.sundayting.moon

import okhttp3.Headers
import retrofit2.Response
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class NetworkResult<T> {

    interface ResponseContent {
        val code: Int
        val httpMessage: String
        val headers: Headers
    }

    class Success<T>(response: Response<T>) : NetworkResult<T>(), ResponseContent {

        init {
            assert(response.body() != null)
            assert(response.isSuccessful)
        }

        val body by lazy { response.body()!! }
        override val code by lazy { response.code() }
        override val httpMessage by lazy { response.message() }
        override val headers by lazy { response.headers() }
    }

    sealed class Failure<T> : NetworkResult<T>() {

        class Exception<T>(val exception: Throwable) : Failure<T>()

        class StatusError<T>(response: Response<T>) : Failure<T>(), ResponseContent {

            init {
                assert(!response.isSuccessful)
            }

            val errorBody by lazy { response.errorBody()!!.string() }
            override val code by lazy { response.code() }
            override val httpMessage by lazy { response.message() }
            override val headers by lazy { response.headers() }
        }

    }

}

@OptIn(ExperimentalContracts::class)
fun <T> NetworkResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is NetworkResult.Success)
        returns(false) implies (this@isSuccess is NetworkResult.Failure)
    }
    return this is NetworkResult.Success
}

@OptIn(ExperimentalContracts::class)
fun <T> NetworkResult<T>.isException(): Boolean {
    contract {
        returns(true) implies (this@isException is NetworkResult.Failure.Exception)
    }
    return this is NetworkResult.Failure.Exception
}

@OptIn(ExperimentalContracts::class)
fun <T> NetworkResult<T>.isStatusError(): Boolean {
    contract {
        returns(true) implies (this@isStatusError is NetworkResult.Failure.StatusError)
    }
    return this is NetworkResult.Failure.StatusError
}

@OptIn(ExperimentalContracts::class)
fun <T> NetworkResult.Failure<T>.isException(): Boolean {
    contract {
        returns(true) implies (this@isException is NetworkResult.Failure.Exception)
        returns(false) implies (this@isException is NetworkResult.Failure.StatusError)
    }
    return this is NetworkResult.Failure.Exception
}

@OptIn(ExperimentalContracts::class)
fun <T> NetworkResult.Failure<T>.isStatusError(): Boolean {
    contract {
        returns(false) implies (this@isStatusError is NetworkResult.Failure.Exception)
        returns(true) implies (this@isStatusError is NetworkResult.Failure.StatusError)
    }
    return this is NetworkResult.Failure.StatusError
}
