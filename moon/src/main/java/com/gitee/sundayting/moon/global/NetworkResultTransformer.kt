package com.gitee.sundayting.moon.global

import com.gitee.sundayting.moon.NResult

interface NetworkResultTransformer {

    /**
     * 拦截网络请求，
     */
    fun <T> transformerBy(result: NResult<T>): NResult<T> {
        return result
    }

    //默认实现
    object Default : NetworkResultTransformer

}