package com.gitee.sundayting.moon.ktx

import com.gitee.sundayting.moon.Result
import retrofit2.Response

//fun ResponseBody.downloadAndCount(
//    dest: File,
//    onDownloadListener: DownloadListener
//) {
//    val contentLength = contentLength()
//    var totalRead = 0L
//    try {
//        if (!dest.exists()) {
//            dest.createNewFile()
//        }
//        dest.sink().buffer().use { bufferedSink ->
//            bufferedSink.writeAll(object : ForwardingSource(source()) {
//                override fun read(sink: Buffer, byteCount: Long): Long {
//                    val bytesRead = super.read(sink, byteCount)
//                    if (bytesRead != -1L) {
//                        totalRead += bytesRead
//                        onDownloadListener.onProgressChange(totalRead, contentLength)
//                    }
//                    return bytesRead
//                }
//            })
//        }
//    } catch (e: Exception) {
//        onDownloadListener.onStop(
//            totalBytes = contentLength, downloadBytes = totalRead,
//            exception = e
//        )
//    } finally {
//        onDownloadListener.onFinish(totalBytes = contentLength, downloadBytes = totalRead)
//    }
//}

interface DownloadListener {
    fun onProgressChange(downloadBytes: Long, totalBytes: Long)
    fun onFinish(downloadBytes: Long, totalBytes: Long) {}
    fun onStop(downloadBytes: Long, totalBytes: Long, exception: Exception?) {}
}

fun <T> Response<T>.toResult(transform: (Result<T>) -> Result<T>): Result<T> {
    return transform(
        if (isSuccessful) {
            toSuccessResult()
        } else {
            Result.ServerErrorException(this).toExceptionResult()
        }
    )
}


fun <T> Response<T>.toSuccessResult(): Result.Success<T> {
    return Result.Success(this)
}

fun Throwable.toExceptionResult(): Result.Failure {
    return Result.Failure(this)
}
