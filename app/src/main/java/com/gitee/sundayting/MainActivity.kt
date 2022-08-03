package com.gitee.sundayting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gitee.moon.ifFailure
import com.gitee.moon.ifSuccess
import com.gitee.sundayting.network.RetrofitUtil
import com.gitee.sundayting.network.WanService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val service: WanService = RetrofitUtil.instance.create(WanService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            service.getArticle(0).ifSuccess {
                Toast.makeText(this@MainActivity, "请求成功！", Toast.LENGTH_LONG).show()
            }.ifFailure {
                Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

}