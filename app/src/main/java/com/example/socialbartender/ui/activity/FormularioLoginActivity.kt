package com.example.socialbartender.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.database.dao.LoginDao
import com.example.socialbartender.databinding.ActivityFormularioLoginBinding
import com.example.socialbartender.model.Login
import java.security.MessageDigest

class FormularioLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioLoginBinding
    private val loginDao: LoginDao by lazy {
        val db = SocialBartenderDatabase.instancia(this)
        db.loginDao()
    }
    private var login: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuraBotaoEntrar()
        configuraBotaoCadastro()
        filtroLogin()
        criaLoginAdmin()


    }


    private fun criaLoginAdmin(){
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("is_first_run", true)

        if (isFirstRun) {
            // Código a ser executado apenas na primeira vez que o aplicativo é executado
            val admin = criaLogin()

            loginDao.salva(admin)

            // Salvar o valor booleano para indicar que o código já foi executado
            prefs.edit().putBoolean("is_first_run", false).apply()
        }
    }

    private fun filtroLogin() {
        // Adiciona o filtro ao campo de login
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Character.isLetter(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }
        binding.activityLoginLogin.filters = arrayOf(filter)
    }
    private fun configuraBotaoEntrar() {
        val botaoEntrar = binding.activityLoginEntrar
        botaoEntrar.setOnClickListener {

            val campoLogin = binding.activityLoginLogin
            val loginText = campoLogin.text.toString()
            val campoSenha = binding.activityLoginSenha
            val senha = campoSenha.text.toString()
            val md = MessageDigest.getInstance("SHA-256")
            val hashSenha = md.digest(senha.toByteArray())

            login = loginDao.buscaPorLoginSenha(loginText,hashSenha)
            login?.let {  val intent = Intent(
                this,
                ListaReceitasActivity::class.java).apply {
                putExtra(CHAVE_LOGIN_ID, it.id)
                }
                startActivity(intent)
            } ?:
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("Login ou senha não encontrado")
                    .setPositiveButton("OK"){_,_ ->
                    }
                    .show()

            }
        }


    private fun criaLogin(): Login {

        val senha = "1234"
        // Gerar o hash da senha usando SHA-256
        val md = MessageDigest.getInstance("SHA-256")
        val hashSenha = md.digest(senha.toByteArray())


        return Login(
            id = 1L,
            nome = "Administrador",
            sobrenome = "SocialBartender",
            login = "admin",
            senha = hashSenha,
            validado = "Sim"
        )

    }


    private fun configuraBotaoCadastro() {
        val botaoCadastro = binding.activityLoginCadastrar
        botaoCadastro.setOnClickListener {
            val intent = Intent(
                this,
                FormularioCadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}
