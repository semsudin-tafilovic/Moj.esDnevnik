package rs.tafilovic.mojesdnevnik.ui.activitiy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import rs.tafilovic.mojesdnevnik.MyApp
import rs.tafilovic.mojesdnevnik.R
import rs.tafilovic.mojesdnevnik.databinding.ActivityLoginBinding
import rs.tafilovic.mojesdnevnik.model.StatusCode
import rs.tafilovic.mojesdnevnik.ui.fragment.LicenceAgreementFragment
import rs.tafilovic.mojesdnevnik.util.*
import rs.tafilovic.mojesdnevnik.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    override val TAG = this::class.java.name

    private val ACCEPTED_LICENCE_AGREEMENT = "ACCEPTED_LICENCE_AGREEMENT"

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var prefsHelper: PrefsHelper

    private var username: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApp).appComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loginViewModel.studentsLiveData.observe(this, Observer {
            Logger.d(TAG, "Students: $it")
            if (it != null) {
                this.setPrefsBoolean(
                    ACCEPTED_LICENCE_AGREEMENT,
                    binding.cbAcceptAgreement.isChecked
                )
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                binding.cardLogin.visibility = View.VISIBLE
                binding.loginProgress.visibility = View.GONE
            }
        })

        loginViewModel.stateLiveData.observe(this, Observer {
            if (it.statusValue == StatusCode.LOADING) {
                binding.loginProgress.visibility = View.VISIBLE
                binding.cardLogin.visibility = View.GONE
            } else {
                binding.loginProgress.visibility = View.GONE
                binding.cardLogin.visibility = View.VISIBLE
            }
            showToast(it.message)
        })

        binding.cbAcceptAgreement.setOnCheckedChangeListener { _, isChecked ->
            binding.btnLogin.isEnabled = isChecked
        }

        binding.btnLogin.setOnClickListener { login() }

        binding.etUsername.onFocusChangeListener = editTextFocusChangedListener
        binding.etPassword.onFocusChangeListener = editTextFocusChangedListener

        KeyboardEventListener(this) {
            binding.tvTitle.visibility = if (it) View.GONE else View.VISIBLE
        }

        binding.tvLicenceAgreement.setOnClickListener {
            LicenceAgreementFragment().show(
                supportFragmentManager,
                LicenceAgreementFragment::class.java.simpleName
            )
        }

        binding.cbAcceptAgreement.isChecked = this.getPrefsBoolean(ACCEPTED_LICENCE_AGREEMENT)
    }

    private fun login() {
        prefsHelper.removeCredentials()
        username = binding.etUsername.editableText?.toString()?.trim()
        password = binding.etPassword.editableText?.toString()?.trim()
        Logger.d(TAG, "btnLoginClick: username: $username password: $password")
        val rememberMe = binding.cbSaveMe.isChecked
        loginViewModel.login(username, password, rememberMe)
    }

    private val editTextFocusChangedListener = View.OnFocusChangeListener { v, hasFocus ->
        val hint: String = when (v?.id) {
            binding.etUsername.id -> getString(R.string.example_google_com)
            else -> getString(R.string.enter_password_here)
        }

        val delay = if (hasFocus) 200L else 0L
        Handler(Looper.getMainLooper()).postDelayed({
            (v as TextInputEditText).hint = if (hasFocus) hint else ""
        }, delay)
    }

    override fun onConnectionChanged(connected: Boolean) {
        super.onConnectionChanged(connected)
        //binding.tvNoInternetConnection.visibility = if (connected) View.GONE else View.VISIBLE
        binding.btnLogin.isEnabled = connected && binding.cbAcceptAgreement.isChecked
    }
}
