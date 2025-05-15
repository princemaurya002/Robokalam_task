package com.princemaurya.robokalam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.princemaurya.robokalam.data.UserPreferencesManager
import com.princemaurya.robokalam.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    
    private val SPLASH_DELAY: Long = 2000 // 2 seconds
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Hide the action bar
        supportActionBar?.hide()

        // Initialize UserPreferencesManager
        userPreferencesManager = UserPreferencesManager(this)

        // Handler to delay the transition to appropriate activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (userPreferencesManager.isLoggedIn()) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
} 