package com.example.socialbartender.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Receita(
        @PrimaryKey(autoGenerate = true) val id: Long =0L,
        val nome: String,
        val ingredientes: String,
        val preparo: String,
        val loginId: Long,
        val imagem: String? = null
) :Parcelable

