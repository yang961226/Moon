package com.gitee.sundayting.moon

import com.gitee.sundayting.moon.MoonInitializer.Companion.init
import com.gitee.sundayting.moon.global.ExceptionMsgNativeLangTransformer
import com.gitee.sundayting.moon.global.NetworkResultTransformer

/**
 * 初始化工具，使用Halo之前必须调用[init]进行初始化
 */
class MoonInitializer private constructor(
    val resultTransformer: NetworkResultTransformer,
    val nativeLangTransformer: ExceptionMsgNativeLangTransformer
) {

    companion object {
        private var _instance: MoonInitializer? = null
        val instance: MoonInitializer by lazy { _instance!! }

        fun init(
            resultTransformer: NetworkResultTransformer = NetworkResultTransformer.Default,
            nativeLangTransformer: ExceptionMsgNativeLangTransformer = ExceptionMsgNativeLangTransformer.Default
        ) {
            check(_instance == null) { "不能多次初始化！" }
            _instance = MoonInitializer(
                resultTransformer = resultTransformer,
                nativeLangTransformer = nativeLangTransformer
            )
        }
    }


}