package com.example.socialbartender.ui.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Switch
import com.example.socialbartender.R
import com.example.socialbartender.database.SocialBartenderDatabase
import com.example.socialbartender.database.dao.LoginDao
import com.example.socialbartender.databinding.ActivityFormularioCadastroUsuarioBinding
import com.example.socialbartender.model.Login
import java.security.MessageDigest

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var loginCarregado: Login
    private lateinit var binding:ActivityFormularioCadastroUsuarioBinding
    private val loginDao: LoginDao by lazy {
        val db = SocialBartenderDatabase.instancia(this)
        db.loginDao()
    }
    private var loginId = 0L
    private var hashSenhaCarregado = "Não"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filtroLogin()

        configuraBotaoSalvar()
        tentaCarregarLogin()
        configuraSwitchTroca()

    }
    override fun onResume() {
        super.onResume()
        tentaBuscarLoginDb()
    }

    private fun tentaBuscarLoginDb() {
        title = "Edite seu Cadastro"
        loginDao.buscaPorId(loginId)?.let { preencheCampos(it)
            val switchMaior = binding.activityFormularioCadastroUsuarioMaior
            val botaoSalvar = binding.activityFormularioCadastroUsuarioSalva
            val campoSenha = binding.activityFormularioCadastroUsuarioSenha
            val campoConfirma = binding.activityFormularioCadastroUsuarioConfirma
            loginCarregado=it
            campoConfirma.setVisibility(View.GONE)
            campoSenha.setVisibility(View.GONE)
            switchMaior.setVisibility(View.GONE)
            botaoSalvar.setVisibility(View.VISIBLE)}
            ?: run {
                title= "Cadastre-se"
                val troca = binding.activityFormularioCadastroUsuarioTroca
                troca.setVisibility(View.GONE)
            }
    }

    private fun preencheCampos(login: Login) {


        binding.activityFormularioCadastroUsuarioNome.setText(login.nome)
        binding.activityFormularioCadastroUsuarioSobrenome.setText(login.sobrenome)
        binding.activityFormularioCadastroUsuarioLogin.setText(login.login)

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

        //Adiciona filtro nome

        val filterNome = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Character.isLetter(source[i]) && !Character.isWhitespace(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }

        binding.activityFormularioCadastroUsuarioLogin.filters = arrayOf(filter)
        binding.activityFormularioCadastroUsuarioNome.filters = arrayOf(filterNome)
        binding.activityFormularioCadastroUsuarioSobrenome.filters = arrayOf(filterNome)
    }

    private fun ByteArray.toHex(): String {
        return joinToString("") { byte -> "%02x".format(byte) }
    }


    private fun configuraSwitchTroca(){
        val switchTroca = findViewById<Switch>(R.id.activity_formulario_cadastro_usuario_troca)
        switchTroca.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val campoSenha = binding.activityFormularioCadastroUsuarioSenha
                val campoConfirma = binding.activityFormularioCadastroUsuarioConfirma
                campoConfirma.setVisibility(View.VISIBLE)
                campoSenha.setVisibility(View.VISIBLE)
                hashSenhaCarregado="Não"}
            else{val campoSenha = binding.activityFormularioCadastroUsuarioSenha
                val campoConfirma = binding.activityFormularioCadastroUsuarioConfirma
                campoConfirma.setVisibility(View.GONE)
                campoSenha.setVisibility(View.GONE)
            hashSenhaCarregado="Sim"}
        }

    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioCadastroUsuarioSalva
        botaoSalvar.setVisibility(View.INVISIBLE)
        val mySwitch = findViewById<Switch>(R.id.activity_formulario_cadastro_usuario_maior)

        mySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
            botaoSalvar.setVisibility(View.VISIBLE)}
            else{botaoSalvar.setVisibility(View.INVISIBLE)}
        }

        botaoSalvar.setOnClickListener {
            val loginNovo = criaLogin()
            val loginExiste = loginDao.buscaPorLogin(loginNovo.login)

            if(loginNovo.validado!="Sim" && hashSenhaCarregado=="Não"){
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("Senhas não são as mesmas ou estão vazias. Corrija e tente novamente.")
                    .setPositiveButton("OK"){_,_ ->
                    }
                    .show()

            }
            else{ if(loginExiste?.login==loginNovo.login && loginId==0L){
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("Login ja existe. Escolha outro e tente novamente.")
                    .setPositiveButton("OK"){_,_ ->
                    }

                    .show()
                }else {
                    if(loginNovo.login=="" ||loginNovo.nome=="" ||loginNovo.sobrenome==""){
                        androidx.appcompat.app.AlertDialog.Builder(this)
                        .setMessage("Todos os campos devem estar preenchidos")
                        .setPositiveButton("OK"){_,_ ->
                        }

                        .show()

                    }else{

                        loginDao.salva(loginNovo)
                        androidx.appcompat.app.AlertDialog.Builder(this)
                        .setMessage("Login Cadastrado/Alterado com Sucesso!")
                        .setPositiveButton("OK") { _, _ ->
                            finish()
                            }
                        .show()
                        }
                }

            }
        }
    }

    private fun tentaCarregarLogin() {
        loginId = intent.getLongExtra(CHAVE_LOGIN_ID, 0L)

    }

    private fun criaLogin(): Login {
        // Acessar os campos de texto usando a referência da classe de binding
        val campoNome = binding.activityFormularioCadastroUsuarioNome
        val nome = campoNome.text.toString()
        val campoSobrenome = binding.activityFormularioCadastroUsuarioSobrenome
        val sobrenome = campoSobrenome.text.toString()
        val campoLogin = binding.activityFormularioCadastroUsuarioLogin
        val login = campoLogin.text.toString()
        val campoSenha = binding.activityFormularioCadastroUsuarioSenha
        val senha = campoSenha.text.toString()
        val campoConfirma = binding.activityFormularioCadastroUsuarioConfirma
        val confirma = campoConfirma.text.toString()

        // Gerar o hash da senha usando SHA-256
        val md = MessageDigest.getInstance("SHA-256")
        val hashSenha = md.digest(senha.toByteArray())
        var validado = "Sim"
        if (senha!=confirma||senha==""||confirma==""){ validado = "Não"
        }
        var senhaEnviada: ByteArray
        if(hashSenhaCarregado=="Sim"){senhaEnviada=loginCarregado.senha}
        else{senhaEnviada=hashSenha}
        return Login(
                id = loginId,
                nome = nome,
                sobrenome = sobrenome,
                login = login,
                senha = senhaEnviada,
                validado = validado
            )

    }
}