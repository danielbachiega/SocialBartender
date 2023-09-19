package com.example.socialbartender.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.database.dao.ReceitaDao
import com.example.socialbartender.model.Receita
import com.example.socialbartender.databinding.ActivityFormularioReceitaBinding
import com.example.socialbartender.extensions.tentaCarregarImagem
import com.example.socialbartender.ui.dialog.FormularioImagemDialog


class FormularioReceitaActivity :
    AppCompatActivity() {


    private lateinit var binding: ActivityFormularioReceitaBinding

    private var url: String? = null
    private var loginId: Long = 0L
    private var receitaId = 0L
    private val receitaDao: ReceitaDao by lazy {
        val db = SocialBartenderDatabase.instancia(this)
        db.receitaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioReceitaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuraBotaoSalvar()

        binding.activityFomutarioReceitaImagem.setOnClickListener {

            FormularioImagemDialog(this).mostra{imagem ->
                url = imagem
                binding.activityFomutarioReceitaImagem.tentaCarregarImagem(url)
            }
        }
        tentaCarregarReceita()
        tentaCarregarLogin()

        }

    override fun onResume() {
        super.onResume()
        tentaBuscarReceitaDb()
    }

    private fun tentaBuscarReceitaDb() {
        title = "Edite seu Drink"
        receitaDao.buscaPorId(receitaId)?.let { preencheCampos(it) }
            ?: run {
                title= "Cadastre seu Drink"
            }
    }

    private fun tentaCarregarReceita() {
        receitaId = intent.getLongExtra(CHAVE_RECEITA_ID, 0L)
    }
    private fun tentaCarregarLogin() {
        loginId = intent.getLongExtra(CHAVE_LOGIN_ID, 0L)
    }


    private fun preencheCampos(receita: Receita) {

        url = receita.imagem
        binding.activityFomutarioReceitaImagem.tentaCarregarImagem(receita.imagem)
        binding.activityFormularioReceitaNome.setText(receita.nome)
        binding.activityFormularioReceitaIngredientes.setText(receita.ingredientes)
        binding.activityFormularioReceitaPreparo.setText(receita.preparo)
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioReceitaBotaoSalvar
        botaoSalvar.setOnClickListener {
            val receitaNova = criaReceita()
            if(receitaNova.nome==""||receitaNova.ingredientes==""||receitaNova.preparo==""){
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("Todos os campos devem estar preenchidos")
                    .setPositiveButton("OK"){_,_ ->
                    }

                    .show()
            }else {

                receitaDao.salva(receitaNova)
                finish()
            }

        }
    }

    private fun criaReceita(): Receita {
        // Acessar os campos de texto usando a referência da classe de binding
        val campoNome = binding.activityFormularioReceitaNome
        val nome = campoNome.text.toString()
        val campoIngredientes = binding.activityFormularioReceitaIngredientes
        val ingredientes = campoIngredientes.text.toString()
        val campoPreparo = binding.activityFormularioReceitaPreparo
        val preparo = campoPreparo.text.toString()

        return Receita(
            id = receitaId,
            nome = nome,
            ingredientes = ingredientes,
            preparo = preparo,
            loginId = loginId,
            imagem = url
        )
    }

}