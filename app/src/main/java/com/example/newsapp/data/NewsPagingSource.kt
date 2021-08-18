package com.example.newsapp.data

import androidx.paging.PagingSource
import com.example.newsapp.api.NewsAPI
import retrofit2.HttpException
import java.io.IOException

private const val START_PAGE_INDEX = 1

class NewsPagingSource(private val newsAPI: NewsAPI, private val query : String) : PagingSource<Int, NewsData>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsData> {
       val position = params.key ?:START_PAGE_INDEX

        return try {
            val response = newsAPI.searchNews(query,position)
            val news = response.articles
            val numberOfResults = response.totalResults

            LoadResult.Page(
                data = news,
                prevKey =  if(position == START_PAGE_INDEX) null else position-1,
                nextKey = if(news.isEmpty()) null else position+1
            )
        }catch (exception : IOException){
            LoadResult.Error(exception)
        }
        catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }
}