package com.gitee.sundayting.moon.global

/**
 * 异常文本自然语言转换器，用于将异常文本翻译成自然语言
 */
interface ExceptionMsgNativeLangTransformer {

    /**
     * 将异常转换成自然语言报错文本，例如"网络异常"
     */
    fun rawToNativeMsg(throwable: Throwable): String

    object Default : ExceptionMsgNativeLangTransformer {

        override fun rawToNativeMsg(throwable: Throwable) = throwable.localizedMessage.orEmpty()

    }

}