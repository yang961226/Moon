package com.gitee.sundayting.network

import com.gitee.sundayting.moon.NResult
import com.gitee.sundayting.network.bean.ArticleBean
import com.gitee.sundayting.network.bean.BannerBean
import com.gitee.sundayting.network.bean.ListBean
import retrofit2.http.GET
import retrofit2.http.Path

interface WanService {

    /**
     * 首页Banner
     */
    @GET("/banner/json")
    suspend fun getBanner(): NResult<WanBeanWrapper<List<BannerBean>>>

    /**
     * 获取首页文章数据
     */
    @GET("/article/list/{page}/json")
    suspend fun getArticle(
        @Path("page") page: Int
    ): NResult<WanBeanWrapper<ListBean<ArticleBean>>>

}
