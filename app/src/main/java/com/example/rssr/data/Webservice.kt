package com.example.rssr.data

import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Webservice{

    suspend fun fetchArticles(url: String): List<ArticleEntity> {
        return withContext(Dispatchers.IO) {
            val parser = Parser()
            try {
                val list: List<Article> = parser.getArticles(url)
                list.map { article ->
                    ArticleEntity(
                        title = article.title ?: "",
                        pubDate = article.pubDate ?: "",
                        link = article.link ?: "",
                        mainImage = article.image ?: "",
                        content = article.content ?: "",
                        description = article.description ?: ""
                    )
                }
            } catch (ex: Exception) {
                listOf<ArticleEntity>()
            }
        }
    }
}