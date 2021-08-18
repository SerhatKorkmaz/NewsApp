package com.example.newsapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.newsapp.api.NewsAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepo @Inject constructor(private val newsapi : NewsAPI) {

    fun getResults(query : String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {NewsPagingSource(newsapi,query)}
    ).liveData
}