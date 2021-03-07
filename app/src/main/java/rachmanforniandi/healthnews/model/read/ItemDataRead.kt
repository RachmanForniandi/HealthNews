package com.rachmanforniandi.mahasiswacrudapp.model.read


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemDataRead(
    @SerializedName("alamat")
    val alamat: String? = null,
    @SerializedName("fakultas")
    val fakultas: String? = null,
    @SerializedName("hobi")
    val hobi: String? = null,
    @SerializedName("id")
    val id : String? = null,
    @SerializedName("jurusan")
    val jurusan: String? = null,
    @SerializedName("nama")
    val nama: String? = null,
    @SerializedName("nik")
    val nik: String? = null,
    @SerializedName("no_hp")
    val noHp: String? = null
): Serializable