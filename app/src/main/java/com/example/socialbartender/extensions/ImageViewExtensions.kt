package com.example.socialbartender.extensions

import android.widget.ImageView
import coil.load
import com.example.socialbartender.R
fun ImageView.tentaCarregarImagem(url: String? = null){
    load(url){
        fallback(R.drawable.icone)
        error(R.drawable.icone)
    }
}