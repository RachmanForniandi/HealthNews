package com.rachmanforniandi.mahasiswacrudapp.model.read


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemDataRead(
    @SerializedName("id")
    val id : String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("content_news")
    val content_news: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("image")
    val image: String? = null
): Serializable