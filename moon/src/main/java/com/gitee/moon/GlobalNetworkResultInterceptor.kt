package com.gitee.moon

interface GlobalNetworkResultInterceptor {

    /**
     * 拦截网络请求，
     */
    fun <T> onIntercept(networkResult: NetworkResult<T>): NetworkResult<T> {
        return networkResult
    }

    //默认实现
    object Default : GlobalNetworkResultInterceptor

}