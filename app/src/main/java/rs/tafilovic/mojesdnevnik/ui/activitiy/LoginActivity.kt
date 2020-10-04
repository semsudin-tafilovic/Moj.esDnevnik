package rs.tafilovic.mojesdnevnik.ui.activitiy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.util.KeyboardEventListener
import rs.tafilovic.mojesdnevnik.util.Logger
import rs.tafilovic.mojesdnevnik.util.PrefsHelper
import rs.tafilovic.mojesdnevnik.viewmodel.LoginViewModel
import java.lang.RuntimeException
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    override val TAG = this::class.java.name

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var prefsHelper: PrefsHelper

    private var username: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApp).appComponent().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel.studentsLiveData.observe(this, Observer {
            Logger.d(TAG, "Students: $it")
            if (it != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                cardLogin.visibility = View.VISIBLE
                loginProgress.visibility = View.GONE
            }
        })

        loginViewModel.stateLiveData.observe(this, Observer {
            if (it.statusValue == StatusCode.LOADING) {
                loginProgress.visibility = View.VISIBLE
                cardLogin.visibility = View.GONE
            } else {
                loginProgress.visibility = View.GONE
                cardLogin.visibility = View.VISIBLE
            }
            showToast(it.message)
        })

        btnLogin.setOnClickListener { login() }

        etUsername.onFocusChangeListener = editTextFocusChangedListener
        etPassword.onFocusChangeListener = editTextFocusChangedListener

        KeyboardEventListener(this) {
            tvTitle.visibility = if (it) View.GONE else View.VISIBLE
            tvDisclaimer.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    private fun login() {
        prefsHelper.removeCredentials()
        username = etUsername.editableText?.toString()
        password = etPassword.editableText?.toString()
        Logger.d(TAG, "btnLoginClick: username: $username password: $password")
        val rememberMe = cbSaveMe.isChecked
        loginViewModel.login(username, password, rememberMe)
    }

    private val editTextFocusChangedListener = View.OnFocusChangeListener { v, hasFocus ->
        val hint: String = when (v?.id) {
            etUsername.id -> getString(R.string.example_google_com)
            else -> getString(R.string.enter_password_here)
        }

        val delay = if (hasFocus) 200L else 0L
        Handler().postDelayed({
            (v as TextInputEditText).hint = if (hasFocus) hint else ""
        }, delay)
    }

    override fun onConnectionChanged(connected: Boolean) {
        super.onConnectionChanged(connected)
        tvNoInternetConnection.visibility = if (connected) View.GONE else View.VISIBLE
        btnLogin.isEnabled = connected
    }
}
