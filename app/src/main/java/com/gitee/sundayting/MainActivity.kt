package com.gitee.sundayting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gitee.moon.ifFailure
import com.gitee.moon.ifSuccess
import com.gitee.sundayting.network.RetrofitUtil
import com.gitee.sundayting.network.WanService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val service = RetrofitUtil.instance.create(WanService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            service.getArticle(0).ifSuccess {
                Log.d("日志", "成功：$it")
            }.ifFailure {
                Log.d("日志", "失败：$it")
            }
        }
    }

}