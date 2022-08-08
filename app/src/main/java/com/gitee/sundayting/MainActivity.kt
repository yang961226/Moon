package com.gitee.sundayting

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gitee.sundayting.moon.isSuccess
import com.gitee.sundayting.network.RetrofitUtil
import com.gitee.sundayting.network.WanService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val service: WanService = RetrofitUtil.instance.create(WanService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            val result = service.getArticle(0)

            if (result.isSuccess()) {
                Toast.makeText(this@MainActivity, "请求成功！", Toast.LENGTH_LONG).show()
                Log.d("请求结果", "成功：${result.body.data}")
            } else {
                Toast.makeText(this@MainActivity, "请求失败", Toast.LENGTH_SHORT).show()
                Log.d("请求结果", "失败")
            }
        }
    }

}