package com.gitee.sundayting.moon.ktx

import com.gitee.sundayting.moon.NResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 提取结果中的实体类，如果是成功请求，则返回实体类，如果为失败请求则返回空
 * @return 网络请求结果对应的实体类
 */
fun <T> NResult<T>.getOrNull(): T? {
    return if (isNSuccess()) {
        body
    } else {
        null
    }
}

/**
 * 提取结果中的实体类，如果是成功请求，则返回实体类，如果为失败请求则返回默认值
 * @param default 默认值
 * @return 网络请求结果对应的实体类
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> NResult<T>.getOrElse(default: () -> T): T {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNSuccess()) {
        body
    } else {
        default()
    }
}

/**
 * 提取结果中的实体类，如果是成功请求，则返回实体类，如果为失败请求则抛出异常
 * @return 网络请求结果对应的实体类
 */
@Throws(Throwable::class)
fun <T> NResult<T>.getOrThrow(): T {
    if (isNSuccess()) {
        return body
    } else {
        throw nException.cause
    }
}

/**
 * 当前网络请求是否是成功请求
 */
@OptIn(ExperimentalContracts::class)
fun <T> NResult<T>.isNSuccess(): Boolean {
    contract {
        returns(true) implies (this@isNSuccess is NResult.NSuccess)
        returns(false) implies (this@isNSuccess is NResult.NFailure)
    }
    return this is NResult.NSuccess
}
