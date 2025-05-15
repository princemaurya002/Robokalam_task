package com.princemaurya.robokalam.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.princemaurya.robokalam.MainActivity
import com.princemaurya.robokalam.R
import com.princemaurya.robokalam.data.UserPreferencesManager
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    private lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoToSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userPreferencesManager = UserPreferencesManager(this)

        // Check if user is already logged in
        if (userPreferencesManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoToSignup = findViewById(R.id.btnGoToSignup)
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }

        btnGoToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun validateInputs(): Boolean {
        val email = tilEmail.editText?.text.toString()
        val password = tilPassword.editText?.text.toString()
        var isValid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = getString(R.string.invalid_email)
            isValid = false
        } else {
            tilEmail.error = null
        }

        if (password.length < 6) {
            tilPassword.error = getString(R.string.invalid_password)
            isValid = false
        } else {
            tilPassword.error = null
        }

        return isValid
    }

    private fun performLogin() {
        val email = tilEmail.editText?.text.toString()
        val password = tilPassword.editText?.text.toString()

        if (userPreferencesManager.validateUser(email, password)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
        }
    }
} 