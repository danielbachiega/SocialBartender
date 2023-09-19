package com.example.socialbartender.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Login(
        @PrimaryKey(autoGenerate = true) val id: Long =0L,
        val nome: String,
        val sobrenome: String,
        val login: String,
        val senha: ByteArray,
        val validado: String
) :Parcelable {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Login

                if (!senha.contentEquals(other.senha)) return false

                return true
        }

        override fun hashCode(): Int {
                return senha.contentHashCode()
        }
}

