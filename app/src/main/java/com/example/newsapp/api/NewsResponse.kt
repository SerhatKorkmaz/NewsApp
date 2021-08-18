package com.example.newsapp.api

import com.example.newsapp.data.NewsData

data class NewsResponse (
    val totalResults : Int,
    val articles: List<NewsData>
        )
