package com.princemaurya.robokalam

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.card.MaterialCardView
import com.princemaurya.robokalam.data.UserPreferencesManager
import com.princemaurya.robokalam.ui.about.AboutActivity
import com.princemaurya.robokalam.ui.auth.LoginActivity
import com.princemaurya.robokalam.ui.portfolio.PortfolioActivity
import com.princemaurya.robokalam.ui.quote.QuoteActivity

class MainActivity : AppCompatActivity() {

    private lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var tvWelcome: TextView
    private lateinit var tvQuoteOfDay: TextView
    private lateinit var cardPortfolio: MaterialCardView
    private lateinit var cardQuotes: MaterialCardView
    private lateinit var cardAbout: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPreferencesManager = UserPreferencesManager(this)

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        initViews()
        setupClickListeners()
        displayWelcomeMessage()
        displayQuoteOfDay()


    }

    private fun initViews() {
        tvWelcome = findViewById(R.id.tvWelcome)
        tvQuoteOfDay = findViewById(R.id.tvQuoteOfDay)
        cardPortfolio = findViewById(R.id.cardPortfolio)
        cardQuotes = findViewById(R.id.cardQuotes)
        cardAbout = findViewById(R.id.cardAbout)
    }

    private fun setupClickListeners() {
        cardPortfolio.setOnClickListener {
            startActivity(Intent(this, PortfolioActivity::class.java))
        }

        cardQuotes.setOnClickListener {
            startActivity(Intent(this, QuoteActivity::class.java))
        }

        cardAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun displayWelcomeMessage() {
        val userName = userPreferencesManager.getUserName() ?: "Student"
        tvWelcome.text = getString(R.string.welcome_message, userName)
    }

    private fun displayQuoteOfDay() {
        // For now, we'll use a static quote. Later, we can implement a quote API or local quote database
        tvQuoteOfDay.text = "\"Education is not preparation for life; education is life itself.\" - John Dewey"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        userPreferencesManager.logout()
        startActivity(Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
    }
}