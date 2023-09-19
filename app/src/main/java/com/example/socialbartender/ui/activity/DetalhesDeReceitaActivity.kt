package com.example.socialbartender.ui.activity

import  android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import com.example.socialbartender.R
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.database.dao.LoginDao
import com.example.socialbartender.databinding.ActivityDetalhesDeReceitaBinding
import com.example.socialbartender.extensions.tentaCarregarImagem
import com.example.socialbartender.model.Receita

class DetalhesDeReceitaActivity : AppCompatActivity() {

    private val CHAVE_RECEITA_ID: String? = "RECEITA_ID"
    private val CHAVE_LOGIN_ID: String? = "LOGIN_ID"
    private var receitaId: Long = 0L
    private var loginId: Long = 0L
    private var receita: Receita? = null
    private val binding by lazy {
        ActivityDetalhesDeReceitaBinding.inflate(layoutInflater)
    }
    private val receitaDao by lazy {
        SocialBartenderDatabase.instancia(this).receitaDao()
    }
    private val loginDao: LoginDao by lazy {
        val db = SocialBartenderDatabase.instancia(this)
        db.loginDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        tentaCarregarReceita()
        tentaCarregarLogin()
        title = "Detalhes"

    }

    override fun onResume() {
        super.onResume()
        buscaReceitaDb()
        //se user for o criador da receita ou for user admin abre possibilidades de exclusão e edição
        if((loginId==receita?.loginId)|| loginId==1L ){
           supportActionBar?.show()
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(loginId == 1L && receita?.loginId!=1L){
            val editar = menu?.findItem(R.id.menu_detalhes_receita_editar)
            editar?.let{it.isVisible = false}
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun buscaReceitaDb() {
        receita = receitaDao.buscaPorId(receitaId)
        receita?.let {
            preencheCampos(it)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_receita, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


            when(item.itemId){

                R.id.menu_detalhes_receita_remover -> {
                    receita?.let { receitaDao.remove(it) }
                    finish()
                }

                R.id.menu_detalhes_receita_editar -> {
                    Intent(this,FormularioReceitaActivity::class.java).apply {
                        putExtra(CHAVE_RECEITA_ID, receitaId)
                        putExtra(CHAVE_LOGIN_ID,loginId)
                        startActivity(this)
                    }

                }
            }

        return super.onOptionsItemSelected(item)
    }

    private fun tentaCarregarReceita() {
       receitaId = intent.getLongExtra(CHAVE_RECEITA_ID, 0L)
    }

    private fun tentaCarregarLogin() {
        loginId = intent.getLongExtra(CHAVE_LOGIN_ID, 0L)
    }

    private fun preencheCampos(receitaCarregado: Receita) {
        binding.apply {
            activityDetalhesDeReceitaImagem.tentaCarregarImagem(receitaCarregado.imagem)
            activityDetalhesDeReceitaNome.text = stringToEditable( receitaCarregado.nome)
            activityDetalhesDeReceitaIngredientes.text = stringToEditable(receitaCarregado.ingredientes)
            activityDetalhesDeReceitaPreparo.text= stringToEditable(receitaCarregado.preparo)
            val login = loginDao.buscaPorId(receitaCarregado.loginId)
            activityDetalhesDeReceitaUser.text = "Criado por: " + login?.login
        }
    }
    private fun stringToEditable(string: String): Editable {
        return Editable.Factory.getInstance().newEditable(string)
    }
}


