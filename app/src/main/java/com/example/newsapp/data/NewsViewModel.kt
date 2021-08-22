package com.example.newsapp.data

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import javax.inject.Inject


class NewsViewModel @ViewModelInject constructor(
    private val repo: NewsRepo,
    @Assisted state : SavedStateHandle
    ): ViewModel() {

        private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

        val news = currentQuery.switchMap {
            repo.getResults(it).cachedIn(viewModelScope)
        }

        fun searchNews(q: String){
            currentQuery.value = q
        }

        companion object{
            private const val CURRENT_QUERY = "current_query"
            private const val DEFAULT_QUERY = "Besiktas"
        }
}