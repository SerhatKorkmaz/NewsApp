package com.example.newsapp.data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class NewsViewModel @ViewModelInject constructor(
    private val repo: NewsRepo
    ): ViewModel() {

        private val currentQuery = MutableLiveData("Besiktas")

        val news = currentQuery.switchMap {
            repo.getResults(it).cachedIn(viewModelScope)
        }

        fun searchNews(q: String){
            currentQuery.value = q
        }
}