package com.example.socialbartender.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.database.dao.LoginDao
import com.example.socialbartender.model.Receita
import com.example.socialbartender.databinding.ReceitaItemBinding
import com.example.socialbartender.extensions.tentaCarregarImagem

class ListaReceitasAdapter(
    private val context: Context,
    receitas: List<Receita> = emptyList(),
    var quandoClicaNoItem: (receita: Receita) -> Unit = {}
) : RecyclerView.Adapter<ListaReceitasAdapter.ViewHolder>() {

    private val receitas = receitas.toMutableList()
    private val loginDao: LoginDao by lazy {
        val db = SocialBartenderDatabase.instancia(context)
        db.loginDao()
    }

    inner class ViewHolder(private val binding: ReceitaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var receita: Receita

        init {
            itemView.setOnClickListener {
                if (::receita.isInitialized) {
                    quandoClicaNoItem(receita)
                }
            }
        }

        fun vincula(receita: Receita) {
            this.receita = receita
            val nome = binding.receitaItemNome
            nome.text = receita.nome
            val ingredientes = binding.receitaItemIngredientes
            ingredientes.text = receita.ingredientes
            val preparo = binding.receitaItemPreparo
            preparo.text = receita.preparo
            val user = binding.receitaItemUser
            val login = loginDao.buscaPorId(receita.loginId)
            user.text = "Criado por: " + login?.login

            binding.receitaItemImagem.tentaCarregarImagem(receita.imagem)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ReceitaItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val receita = receitas[position]
        holder.vincula(receita)
    }

    override fun getItemCount(): Int = receitas.size

    fun atualiza(receitas: List<Receita>) {
        this.receitas.clear()
        this.receitas.addAll(receitas)
        notifyDataSetChanged()
    }

}


