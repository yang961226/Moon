package com.gitee.sundayting

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gitee.sundayting.moon.ktx.getOrElse
import com.gitee.sundayting.moon.ktx.getOrNull
import com.gitee.sundayting.moon.ktx.getOrThrow
import com.gitee.sundayting.moon.ktx.isNSuccess
import com.gitee.sundayting.network.RetrofitUtil
import com.gitee.sundayting.network.WanBeanWrapper
import com.gitee.sundayting.network.WanService
import com.gitee.sundayting.network.bean.ArticleBean
import com.gitee.sundayting.network.bean.ListBean
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val service: WanService = RetrofitUtil.instance.create(WanService::class.java)

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        val tvResult = findViewById<TextView>(R.id.tv_result)
        findViewById<Button>(R.id.btn_start).apply {
            setOnClickListener {
                tvResult.text = "加载中"
                job?.cancel()
                job = lifecycleScope.launch {
                    val result = service.getArticle(0)
                    //标准使用方法：可以获取完整的网络请求结果
                    if (result.isNSuccess()) {
                        tvResult.text = "成功"
                        val body: WanBeanWrapper<ListBean<ArticleBean>> = result.body
                    } else {
                        tvResult.text = result.nException.message
                        Log.d("日志", "失败")
                    }

                    //备选法，如果成功则返回正确结果，如果失败则返回默认结果
                    val tmp1 = result.getOrElse { WanBeanWrapper(data = ListBean()) }

                    //可空法，如果成功则返回正确结果，如果失败则返回空对象
                    val tmp2 = result.getOrNull()

                    //抛出法，如果成功则返回正确结果，如果失败则抛出异常
                    try {
                        val tmp3 = result.getOrThrow()
                    } catch (e: Exception) {

                    }
                }
            }
        }


    }

}