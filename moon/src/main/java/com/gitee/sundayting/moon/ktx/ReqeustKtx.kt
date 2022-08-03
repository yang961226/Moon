package com.gitee.sundayting.moon.ktx

import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Invocation
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.IOException

/**
 * 返回某个Retrofit定义在方法上的注解，例如[POST],[GET]
 */
fun <T : Annotation> Request.getMethodAnnotation(annotationClass: Class<T>): T? {
    return tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)
}

fun <T : Annotation> Request.containMethodAnnotation(annotationClass: Class<T>): Boolean {
    return getMethodAnnotation(annotationClass) != null
}

/**
 * 将请求体的字节流写入字符串
 */
@Throws(IOException::class)
fun RequestBody.writeToString(): String {
    return Buffer().also {
        writeTo(it)
    }.readUtf8()
}
