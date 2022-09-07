package com.gitee.sundayting.moon.exception

import okhttp3.ResponseBody
import okio.IOException

/**
 * 服务器错误导致的异常（非200状态码）
 * @param code http状态码
 * @param errorBody http报错体（内容可能较大，慎重调取）
 */
class ServerErrorException(
    val code: Int,
    val errorBody: ResponseBody?
) : IOException()


