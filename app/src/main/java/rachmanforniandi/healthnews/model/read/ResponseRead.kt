package com.rachmanforniandi.mahasiswacrudapp.model.read


import com.google.gson.annotations.SerializedName

data class ResponseRead(
    @SerializedName("data")
    val data: List<ItemDataRead>,

    @SerializedName("message")
    val message: String
)