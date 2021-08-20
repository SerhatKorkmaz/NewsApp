package com.example.newsapp.api

import com.example.newsapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsAPI {

    companion object{
        const val url = "https://newsapi.org/"
        const val key = BuildConfig.NEWSAPI_ACCESS_KEY
    }
    @GET("v2/everything")
    suspend fun searchNews(
        @Query ("q") q:String,
        @Query ("page") page:Int,
        @Query ("apiKey") apiKey:String = key
    ):NewsResponse
}