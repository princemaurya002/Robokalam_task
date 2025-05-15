package com.princemaurya.robokalam.ui.quote

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.princemaurya.robokalam.R

class QuoteActivity : AppCompatActivity() {
    private val viewModel by viewModels<QuoteViewModel>()

    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tvQuote: TextView
    private lateinit var tvAuthor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.quote_of_day)

        // Initialize views
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        tvQuote = findViewById(R.id.tvQuote)
        tvAuthor = findViewById(R.id.tvAuthor)

        // Observe view model
        viewModel.quote.observe(this) { quote ->
            tvQuote.text = quote.quote
            tvAuthor.text = "- ${quote.author}"
            tvQuote.visibility = View.VISIBLE
            tvAuthor.visibility = View.VISIBLE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                tvError.visibility = View.GONE
                tvQuote.visibility = View.GONE
                tvAuthor.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                tvError.text = error
                tvError.visibility = View.VISIBLE
                tvQuote.visibility = View.GONE
                tvAuthor.visibility = View.GONE
            } else {
                tvError.visibility = View.GONE
            }
        }

        // Fetch quote
        viewModel.fetchDailyQuote()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 