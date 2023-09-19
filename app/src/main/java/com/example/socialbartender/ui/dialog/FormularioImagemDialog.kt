package com.example.socialbartender.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.socialbartender.databinding.FormularioImagemBinding
import com.example.socialbartender.extensions.tentaCarregarImagem

class FormularioImagemDialog(private val context: Context) {

    fun mostra(
        urlPadrao: String? = null,
        quandoImagemCarregada: (imagem: String) -> Unit
    ){
        FormularioImagemBinding
            .inflate(LayoutInflater.from(context)).apply {
                urlPadrao?.let {
                    formularioImagemImageview.tentaCarregarImagem(it)
                    formularioImagemUrl.setText(it)
                }
                urlPadrao?.let {
                    formularioImagemImageview.tentaCarregarImagem(it)
                    formularioImagemUrl.setText(it)
                }

                formularioImagemButton.setOnClickListener {
                    val url = formularioImagemUrl.text.toString()
                    formularioImagemImageview.tentaCarregarImagem(url)
                }

                AlertDialog.Builder(context)
                    .setPositiveButton("Confirmar"){_,_ ->
                        val url = formularioImagemUrl.text.toString()
                        quandoImagemCarregada(url)
                    }
                    .setNegativeButton("Cancelar"){_,_ ->

                    }
                    .setView(root)
                    .show()
            }


    }
}