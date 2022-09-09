package com.gitee.sundayting.moon

import com.gitee.sundayting.moon.internal.MoonResponseCallDelegate
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MoonCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): MoonCallAdapter? =
        when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(callType)) {
                    NResult::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        MoonCallAdapter(resultType)
                    }
                    else -> null
                }
            }
        else -> null
        }

    companion object {

        fun create(): MoonCallAdapterFactory = MoonCallAdapterFactory()

    }

    class MoonCallAdapter constructor(
        private val resultType: Type
    ) : CallAdapter<Type, Call<NResult<Type>>> {

        override fun responseType() = resultType

        override fun adapt(call: Call<Type>): Call<NResult<Type>> =
            MoonResponseCallDelegate(call)

    }

}