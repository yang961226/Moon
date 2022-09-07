package com.gitee.sundayting.moon.ktx

import com.gitee.sundayting.moon.exception.ServerErrorException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * 当前异常为服务器HTTP协议报错
 *
 * @see [ServerErrorException]
 */
@OptIn(ExperimentalContracts::class)
fun Throwable.isServerErrorException(): Boolean {
    contract {
        returns(true) implies (this@isServerErrorException is ServerErrorException)
    }
    return this is ServerErrorException
}

