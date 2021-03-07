package com.rachmanforniandi.mahasiswacrudapp.model.update

import com.google.gson.annotations.SerializedName

data class ResponseUpdate(
    @SerializedName("message")
    val message: String
)