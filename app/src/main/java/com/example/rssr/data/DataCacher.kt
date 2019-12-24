package com.example.rssr.data

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class DataCacher(val appContext: Context){

    fun saveData(articles: List<ArticleEntity>) {
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val moshi = Moshi.Builder().build()

        val type = Types.newParameterizedType(
            List::class.java, ArticleEntity::class.java
        )

        val articlesAdapter: JsonAdapter<List<ArticleEntity>> = moshi.adapter(type)
        val jsonData = articlesAdapter.toJson(articles)
        val editor = sharedPrefs.edit()

        editor.putString(CACHE_DATA_KEY, jsonData)

        editor.apply()
    }

    fun restoreCachedData(): List<ArticleEntity> {
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val jsonData = sharedPrefs.getString(CACHE_DATA_KEY, null)
        if (jsonData != null) {
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                List::class.java, ArticleEntity::class.java
            )
            val articlesAdapter: JsonAdapter<List<ArticleEntity>> = moshi.adapter(type)
            val articles = articlesAdapter.fromJson(jsonData)
            return requireNotNull(articles)
        }
        return listOf()
    }

    fun restoreUrl(): String {
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val restoredUrl = sharedPrefs.getString(URL_KEY, "https://www.androidauthority.com/feed")
        return requireNotNull(restoredUrl)
    }

    fun saveUrl(url: String){
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(URL_KEY, url)
        editor.apply()
    }

    companion object {
        private const val CACHE_DATA_KEY = "CACHE_DATA"
        private const val URL_KEY = "URL_KEY"
        private const val PREF_NAME = "CachedData"
    }
}