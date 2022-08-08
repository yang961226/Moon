package com.gitee.sundayting.moon

interface NetworkResultTransformer {

    /**
     * 拦截网络请求，
     */
    fun <T> transformerBy(result: Result<T>): Result<T> {
        return result
    }

    //默认实现
    object Default : NetworkResultTransformer

}