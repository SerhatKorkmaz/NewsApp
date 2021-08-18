package com.example.newsapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NewsData (
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
    ): Parcelable{

    @Parcelize
    data class source(
        val id : String,
        val name : String
    ):Parcelable
}