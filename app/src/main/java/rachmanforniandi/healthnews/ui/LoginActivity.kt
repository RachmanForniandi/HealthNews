package rachmanforniandi.healthnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rachmanforniandi.mahasiswacrudapp.model.login.ResponseLogin
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import rachmanforniandi.healthnews.R
import rachmanforniandi.healthnews.utility.SessionManager
import rachmanforniandi.healthnews.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        sessionManager = SessionManager(this)

        btn_login.onClick {
            val emailUser = et_login_email.text.toString()
            val passUser = et_login_Password.text.toString()
            sessionManager.saveUserEmail("email", emailUser)
            sessionManager.login = true
            viewModel.loginUser(emailUser,passUser)
        }

        txt_link_to_register.onClick {
            startActivity<RegisterActivity>()
        }

        attachObserveLogin()

    }

    private fun attachObserveLogin() {
        viewModel.isLoading.observe(this,{ it ->showProgress(it) })
        viewModel.loginResponder.observe(this, { showResponse(it) })
        viewModel.apiError.observe(this, { showError(it) })
        viewModel.isEmptyInput.observe(this, { showIsEmpty(it) })
    }

    private fun showIsEmpty(it: Boolean) {
        if (it)toast("Email/Password tidak boleh kosong.")
    }

    private fun showProgress(it: Boolean?) {
        if (it == true) {
            progress_login.visibility = View.VISIBLE
        } else {
            progress_login.visibility = View.GONE
        }
    }

    private fun showResponse(it: ResponseLogin?) {
        if (it?.message.equals("Register user berhasil.")){
            startActivity<MainActivity>()
            finish()
        }else{
            toast(it?.message ?: "")
        }
    }

    private fun showError(it: Throwable?) {
        toast(it?.message ?: "")
    }
}