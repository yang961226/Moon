package com.gitee.sundayting.moon.exception


/**
 * 统一网络异常
 * @param message 错误的原因，能够被人直接阅读的信息，非原生错误栈信息
 * @param cause 原始异常，即触发网络错误的真正异常
 */
class NException internal constructor(
    override val message: String,
    override val cause: Throwable
) : Throwable(message, cause)