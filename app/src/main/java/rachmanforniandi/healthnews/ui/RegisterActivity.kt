package rachmanforniandi.healthnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.rachmanforniandi.mahasiswacrudapp.model.register.ResponseRegister
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import rachmanforniandi.healthnews.R
import rachmanforniandi.healthnews.utility.SessionManager
import rachmanforniandi.healthnews.viewmodels.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        sessionManager = SessionManager(this)

        btn_register.onClick {
            val username = et_register_username.text.toString()
            val passUser = et_register_Password.text.toString()
            val alamatDomisili = et_register_alamat_domisili.text.toString()
            val noHpUser = et_register_noHp.text.toString()
            val emailUser = et_register_email.text.toString()

            viewModel.registerUser(username,passUser,alamatDomisili,noHpUser,emailUser)
            sessionManager.saveUserName("username",username)
            sessionManager.saveUserEmail("email",emailUser)

        }

        observeResponseRegister()
    }

    private fun observeResponseRegister() {
        viewModel.isLoading.observe(this,  { loadingForRegister(it) })
        viewModel.registerResponder.observe(this,{showResponseRegister(it)})
        viewModel.apiError.observe(this,  { showErrorRegister(it) })
        viewModel.isEmptyInput.observe(this, { showIsEmpty(it) })
    }

    private fun showIsEmpty(it: Boolean) {
        if (it)toast("Mohon input data register diisi semua & tidak boleh kosong.")
    }

    private fun showResponseRegister(it: ResponseRegister?) =
        if (it?.message.equals("Register user berhasil.")){
            startActivity<LoginActivity>()
            finish()
        }else{
            toast(it?.message ?:"")
        }


    private fun loadingForRegister(it: Boolean?) {
        if (it == true) {
            progress_register.visibility =View.VISIBLE
        } else {
            progress_register.visibility = View.GONE
        }

    }
    private fun showErrorRegister(it: Throwable?) {
        toast(it?.message ?: "")
    }
}