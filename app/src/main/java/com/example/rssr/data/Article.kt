package com.example.rssr.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ArticleEntity (
    val title: String,
    val pubDate: String,
    val mainImage: String,
    val content: String,
    val description: String,
    val link: String
) : Parcelable
