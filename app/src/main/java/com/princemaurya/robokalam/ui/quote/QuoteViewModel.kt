package com.princemaurya.robokalam.ui.quote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.princemaurya.robokalam.data.model.Quote
import com.princemaurya.robokalam.data.repository.QuoteRepository
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    private val repository = QuoteRepository()
    private val _quote = MutableLiveData<Quote>()
    val quote: LiveData<Quote> = _quote

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchDailyQuote() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val quotes = repository.getDailyQuote()
                if (quotes.isNotEmpty()) {
                    _quote.value = quotes[0]
                }
//                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load quote"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 