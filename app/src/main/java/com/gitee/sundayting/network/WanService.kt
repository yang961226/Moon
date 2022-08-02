package com.gitee.sundayting.network

import com.gitee.moon.NetworkResult
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
    suspend fun getBanner(): NetworkResult<WanBeanWrapper<List<BannerBean>>>

    /**
     * 获取首页文章数据
     */
    @GET("/article/list/{page}/json")
    suspend fun getArticle(
        @Path("page") page: Int
    ): NetworkResult<WanBeanWrapper<ListBean<ArticleBean>>>

}
