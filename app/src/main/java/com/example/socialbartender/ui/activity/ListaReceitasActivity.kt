package com.example.socialbartender.ui.activity

import com.example.socialbartender.ui.recyclerview.adapter.ListaReceitasAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbartender.R
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.databinding.ActivityListaReceitasActivityBinding


class ListaReceitasActivity : AppCompatActivity() {


    private val adapter = ListaReceitasAdapter(this)
    private var loginId: Long = 0L
    private val binding by lazy {
        ActivityListaReceitasActivityBinding.inflate(layoutInflater)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_receitas, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.menu_lista_receita_sair -> {
               finish()
            }
            R.id.menu_lista_receita_perfil -> {
                Intent(this,DetalhesDeUsuarioActivity::class.java).apply {
                    putExtra(CHAVE_LOGIN_ID, loginId)
                    startActivity(this)
                }
            }
            R.id.menu_lista_receita_search ->{
                val botaoSearch =binding.activityListaReceitasFabPesquisar
                botaoSearch.setVisibility(View.VISIBLE)
                val campoSearch = binding.activityListaReceitasTextinputlayoutPesquisar
                campoSearch.setVisibility(View.VISIBLE)

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraRecyclerView()
        configuraAddFab()
        setContentView(binding.root)
        configuraSearchFab()
        configuraLimpaFab()
        loginId = intent.getLongExtra(CHAVE_LOGIN_ID, 0L)
        val botaoSearch =binding.activityListaReceitasFabPesquisar
        botaoSearch.setVisibility(View.GONE)
        val campoSearch = binding.activityListaReceitasTextinputlayoutPesquisar
        campoSearch.setVisibility(View.GONE)
        val botaoLimpa = binding.activityListaReceitasFabLimpa
        botaoLimpa.setVisibility(View.GONE)

    }

    override fun onResume() {
        super.onResume()
        val db = SocialBartenderDatabase.instancia(this)
        val receitaDao = db.receitaDao()
        adapter.atualiza(receitaDao.buscaTodos())
    }

    private fun configuraAddFab() {
        val fab = binding.activityListaReceitasFab
        fab.setOnClickListener {
            vaiParaFormularioReceita()
        }
    }

    private fun configuraLimpaFab() {
        val fab = binding.activityListaReceitasFabLimpa
        fab.setOnClickListener {
            val db = SocialBartenderDatabase.instancia(this)
            val receitaDao = db.receitaDao()
            val botaoLimpa = binding.activityListaReceitasFabLimpa
            botaoLimpa.setVisibility(View.GONE)
            adapter.atualiza(receitaDao.buscaTodos())
        }
    }

    private fun configuraSearchFab() {
        val fab = binding.activityListaReceitasFabPesquisar
        fab.setOnClickListener {
            val botaoLimpa = binding.activityListaReceitasFabLimpa
            botaoLimpa.setVisibility(View.VISIBLE)
            val botaoSearch =binding.activityListaReceitasFabPesquisar
            botaoSearch.setVisibility(View.GONE)
            val campoSearch = binding.activityListaReceitasTextinputlayoutPesquisar
            campoSearch.setVisibility(View.GONE)
            val db = SocialBartenderDatabase.instancia(this)
            val receitaDao = db.receitaDao()
            val campoPesquisa = binding.activityListaReceitasPesquisar
            val pesquisa = campoPesquisa.text.toString()
            pesquisa.let { receitaDao.buscaPorPesquisa(it)}.let { it1 -> adapter.atualiza(it1) }

        }
    }

    private fun vaiParaFormularioReceita() {
        val intent = Intent(this, FormularioReceitaActivity::class.java).apply {
            putExtra(CHAVE_LOGIN_ID, loginId)
        }
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaReceitasRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this,
                DetalhesDeReceitaActivity::class.java
            ).apply {
                putExtra(CHAVE_RECEITA_ID, it.id)
                putExtra(CHAVE_LOGIN_ID, loginId)
            }
            startActivity(intent)
        }
    }

}