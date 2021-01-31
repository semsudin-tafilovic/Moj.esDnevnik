package rs.tafilovic.mojesdnevnik.ui.activitiy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.ui.fragment.LicenceAgreementFragment
import rs.tafilovic.mojesdnevnik.util.*
import rs.tafilovic.mojesdnevnik.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    override val TAG = this::class.java.name

    private val ACCEPTED_LICENCE_AGREEMENT = "ACCEPTED_LICENCE_AGREEMENT"

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
                this.setPrefsBoolean(ACCEPTED_LICENCE_AGREEMENT, cbAcceptAgreement.isChecked)
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

        cbAcceptAgreement.setOnCheckedChangeListener { buttonView, isChecked ->
            btnLogin.isEnabled = isChecked
        }

        btnLogin.setOnClickListener { login() }

        etUsername.onFocusChangeListener = editTextFocusChangedListener
        etPassword.onFocusChangeListener = editTextFocusChangedListener

        KeyboardEventListener(this) {
            tvTitle.visibility = if (it) View.GONE else View.VISIBLE
        }

        tvLicenceAgreement.setOnClickListener {
            LicenceAgreementFragment().show(
                supportFragmentManager,
                LicenceAgreementFragment::class.java.simpleName
            )
        }

        cbAcceptAgreement.isChecked = this.getPrefsBoolean(ACCEPTED_LICENCE_AGREEMENT)
    }

    private fun login() {
        prefsHelper.removeCredentials()
        username = etUsername.editableText?.toString()?.trim()
        password = etPassword.editableText?.toString()?.trim()
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
        btnLogin.isEnabled = connected && cbAcceptAgreement.isChecked
    }
}
