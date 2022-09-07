package com.gitee.sundayting

import android.app.Application
import com.gitee.sundayting.moon.MoonInitializer
import com.gitee.sundayting.moon.NResult
import com.gitee.sundayting.moon.global.ExceptionMsgNativeLangTransformer
import com.gitee.sundayting.moon.global.NetworkResultTransformer
import com.gitee.sundayting.moon.ktx.isNSuccess
import com.gitee.sundayting.moon.ktx.toNFailure
import com.gitee.sundayting.network.WanBeanWrapper
import com.gitee.sundayting.network.WanErrorException

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MoonInitializer.init(
            resultTransformer = object : NetworkResultTransformer {
                override fun <T> transformerBy(result: NResult<T>): NResult<T> {
                    if (result.isNSuccess() && result.body is WanBeanWrapper<*>) {
                        val wrapper = result.body as WanBeanWrapper<*>
                        if (!wrapper.isSuccessful()) {
                            return WanErrorException(wrapper.errorMsg).toNFailure()
                        }
                    }
                    return result
                }
            },
            nativeLangTransformer = object : ExceptionMsgNativeLangTransformer {
                override fun rawToNativeMsg(throwable: Throwable): String {
                    return "网络错误"
                }
            }
        )
    }

}