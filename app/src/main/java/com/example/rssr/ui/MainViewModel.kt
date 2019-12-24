package com.example.rssr.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rssr.data.ArticleEntity
import com.example.rssr.data.ArticlesRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ArticlesRepository(application)
    private var articles = MutableLiveData<List<ArticleEntity>>()

    init{
        setArticles()
    }

    fun setArticles(){
        viewModelScope.launch {
            articles.value = repository.getArticles()
        }
    }

    fun getArticles(): LiveData<List<ArticleEntity>> {
        return articles
    }

    fun changeUrl(url: String){
        repository.setUrl(url)
    }
}

