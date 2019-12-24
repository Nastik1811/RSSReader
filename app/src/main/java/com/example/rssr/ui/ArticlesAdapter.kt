package com.example.rssr.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import androidx.recyclerview.widget.RecyclerView
import com.example.rssr.R
import com.example.rssr.data.ArticleEntity
import com.example.rssr.utils.DateConverter
import kotlinx.android.synthetic.main.article_item.view.*
import java.text.ParseException

class ArticlesAdapter constructor(context: Context, private val clickListener: (link: String) -> Unit):
    RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var articles = listOf<ArticleEntity>()

    internal fun setArticles(articles: List<ArticleEntity>){
        this.articles = articles
        notifyDataSetChanged()
    }

    internal fun clear()
    {
        articles = listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = inflater.inflate(R.layout.new_artical_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
    val article = articles[position]
        holder.bind(article, clickListener)
    }

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(article: ArticleEntity, clickListener: (url: String) -> Unit){
            itemView.title.text = article.title

            itemView.pub_date.text = try {
                DateConverter.getFormattedDate(article.pubDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                article.pubDate
            }

            if(article.description.isNotEmpty())
                itemView.description.text = article.description
            else{
                val indexOfPreview = article.content.indexOf("<p>")
                if (indexOfPreview != -1) {
                    val re = Regex("""<[^>]+>*""")
                    var answer = article.content.substring(indexOfPreview, indexOfPreview + 100)
                    answer = re.replace(answer, "")
                    itemView.description.text = answer
                } else {
                    itemView.description.text = article.description
                }
            }

            if (article.mainImage == "") {
                itemView.image.visibility = View.GONE
            } else {
                itemView.image.load(article.mainImage)
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                clickListener(articles[position].link)
            }
        }
    }
}

