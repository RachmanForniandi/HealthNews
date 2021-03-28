package rachmanforniandi.healthnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.startActivity
import rachmanforniandi.healthnews.R
import rachmanforniandi.healthnews.utility.SessionManager
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sessionManager = SessionManager(this)
        Timer("splashGone", true).schedule(3000) {
            if (sessionManager.login == true) {
                startActivity<MainActivity>()
                finish()
            }else{
                startActivity<LoginActivity>()
                finish()
            }
        }
    }
}