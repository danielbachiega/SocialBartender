package com.example.socialbartender.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import com.example.socialbartender.R
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.database.dao.LoginDao
import com.example.socialbartender.databinding.ActivityDetalhesDeUsuarioBinding
import com.example.socialbartender.model.Login
import com.example.socialbartender.ui.recyclerview.adapter.ListaReceitasAdapter

class DetalhesDeUsuarioActivity : AppCompatActivity() {

    private val adapter = ListaReceitasAdapter(this)
    private var loginId: Long = 0L
    private var login: Login? = null
    private val binding by lazy {
        ActivityDetalhesDeUsuarioBinding.inflate(layoutInflater)
    }

    private val loginDao: LoginDao by lazy {
        val db = SocialBartenderDatabase.instancia(this)
        db.loginDao()
    }

    private val receitaDao by lazy {
        SocialBartenderDatabase.instancia(this).receitaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // supportActionBar?.hide()
        tentaCarregarLogin()
        setContentView(binding.root)
        configuraRecyclerView()
        title = "Detalhes de Perfil"
    }


    override fun onResume() {
        super.onResume()

        buscaLoginDb()
        val db = SocialBartenderDatabase.instancia(this)
        val receitaDao = db.receitaDao()
        adapter.atualiza(receitaDao.buscaPorLoginId(loginId))

    }


    private fun buscaLoginDb() {
        login = loginDao.buscaPorId(loginId)
        login?.let {
            preencheCampos(it)
        } ?: finish()
    }

    private fun tentaCarregarLogin() {
        loginId = intent.getLongExtra(CHAVE_LOGIN_ID, 0L)
    }


    private fun preencheCampos(loginCarregado: Login) {
        binding.apply {
            val nome = loginCarregado.nome + " " + loginCarregado.sobrenome

            activityDetalhesDeUsuarioNome.text = stringToEditable(nome)
            activityDetalhesDeUsuarioLogin.text = stringToEditable(loginCarregado.login)


        }
    }

    private fun stringToEditable(string: String): Editable {
        return Editable.Factory.getInstance().newEditable(string)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_usuario, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(loginId == 1L){
            val deletar = menu?.findItem(R.id.menu_detalhes_usuario_remover)
            deletar?.let{it.isVisible = false}
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityDetalhesDeUsuarioReceitasRecyclerView
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {

            R.id.menu_detalhes_usuario_remover -> {

                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("Tem certeza que deseja excluir o seu cadastro?\nEssa ação removera todos os seus Drinks publicados!!!")
                    .setPositiveButton("SIM") { _, _ ->
                        val buscaReceitas = login?.let { receitaDao.buscaPorLoginId(it.id) }
                        buscaReceitas?.let {  for (receita in it) {
                            receitaDao.remove(receita)
                        }
                        }
                        login?.let { loginDao.remove(it) }

                        val intent = Intent(this, FormularioLoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finishAffinity()
                    }.setNegativeButton("NÃO"){_,_ ->}
                    .show()

            }

            R.id.menu_detalhes_usuario_editar -> {
                Intent(this, FormularioCadastroUsuarioActivity::class.java).apply {
                    putExtra(CHAVE_LOGIN_ID, loginId)
                    startActivity(this)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }
}