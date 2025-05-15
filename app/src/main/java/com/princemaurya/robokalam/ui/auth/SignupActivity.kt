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

class SignupActivity : AppCompatActivity() {

    private lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var tilName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var btnSignup: MaterialButton
    private lateinit var btnGoToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        userPreferencesManager = UserPreferencesManager(this)
        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        tilName = findViewById(R.id.tilName)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        btnSignup = findViewById(R.id.btnSignup)
        btnGoToLogin = findViewById(R.id.btnGoToLogin)
    }

    private fun setupClickListeners() {
        btnSignup.setOnClickListener {
            if (validateInputs()) {
                performSignup()
            }
        }

        btnGoToLogin.setOnClickListener {
            finish() // Go back to LoginActivity
        }
    }

    private fun validateInputs(): Boolean {
        val name = tilName.editText?.text.toString()
        val email = tilEmail.editText?.text.toString()
        val password = tilPassword.editText?.text.toString()
        var isValid = true

        if (name.isEmpty()) {
            tilName.error = getString(R.string.invalid_name)
            isValid = false
        } else {
            tilName.error = null
        }

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

    private fun performSignup() {
        val name = tilName.editText?.text.toString()
        val email = tilEmail.editText?.text.toString()
        val password = tilPassword.editText?.text.toString()

        userPreferencesManager.saveUser(email, password, name)
        Toast.makeText(this, R.string.signup_success, Toast.LENGTH_SHORT).show()
        
        // Navigate to MainActivity
        startActivity(Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
    }
} 