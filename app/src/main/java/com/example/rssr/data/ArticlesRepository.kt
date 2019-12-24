package com.example.rssr.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

// This class is responsible for providing us with the data
// It track network connection and based on that decide from where to fetch data.
// So here we should check internet connection

@Suppress("DEPRECATION")
class ArticlesRepository (val application: Application){

    private val webservice = Webservice()
    private val dataCacher = DataCacher(application)

    private var _url: String = dataCacher.restoreUrl()

    private fun checkInternetConnection() : Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    suspend fun getArticles(): List<ArticleEntity>{
        val articles: List<ArticleEntity>
        val isConnected = checkInternetConnection()

        if(isConnected){
            articles = webservice.fetchArticles(_url)
            dataCacher.saveData(articles.takeLast(10))
        }
        else
            articles = dataCacher.restoreCachedData()
        return articles
    }

    fun setUrl(url: String){
        this._url = url
        dataCacher.saveUrl(_url)
    }
}



