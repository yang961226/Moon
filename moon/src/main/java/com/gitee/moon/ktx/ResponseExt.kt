package com.gitee.moon.ktx

import com.gitee.moon.GlobalNetworkResultInterceptor
import com.gitee.moon.NetworkResult
import okhttp3.ResponseBody
import okio.Buffer
import okio.ForwardingSource
import okio.buffer
import okio.sink
import retrofit2.Response
import java.io.File

// TODO: 2021/12/29 临时封装方案
fun ResponseBody.downloadAndCount(
    dest: File,
    onDownloadListener: DownloadListener
) {
    val contentLength = contentLength()
    var totalRead = 0L
    try {
        if (!dest.exists()) {
            dest.createNewFile()
        }
        dest.sink().buffer().use { bufferedSink ->
            bufferedSink.writeAll(object : ForwardingSource(source()) {
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    if (bytesRead != -1L) {
                        totalRead += bytesRead
                        onDownloadListener.onProgressChange(totalRead, contentLength)
                    }
                    return bytesRead
                }
            })
        }
    } catch (e: Exception) {
        onDownloadListener.onStop(
            totalBytes = contentLength, downloadBytes = totalRead,
            exception = e
        )
    } finally {
        onDownloadListener.onFinish(totalBytes = contentLength, downloadBytes = totalRead)
    }
}

interface DownloadListener {
    fun onProgressChange(downloadBytes: Long, totalBytes: Long)
    fun onFinish(downloadBytes: Long, totalBytes: Long) {}
    fun onStop(downloadBytes: Long, totalBytes: Long, exception: Exception?) {}
}

fun <T> Response<T>.toNetworkResult(interceptor: GlobalNetworkResultInterceptor): NetworkResult<T> =
    interceptor.onIntercept(
        try {
            if (isSuccessful) {
                toSuccessResult()
            } else {
                toServerErrorResult()
            }
        } catch (t: Throwable) {
            t.toExceptionResult()
        }
    )

fun <T> Response<T>.toSuccessResult(): NetworkResult.Success<T> {
    return NetworkResult.Success(this)
}

fun <T> Response<T>.toServerErrorResult(): NetworkResult.Failure.ServerError<T> {
    return NetworkResult.Failure.ServerError(this)
}

fun <T> Throwable.toExceptionResult(): NetworkResult.Failure.Exception<T> {
    return NetworkResult.Failure.Exception(this)
}