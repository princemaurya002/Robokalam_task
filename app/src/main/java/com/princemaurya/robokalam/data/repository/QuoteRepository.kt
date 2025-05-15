package com.princemaurya.robokalam.data.repository

import com.princemaurya.robokalam.data.api.QuoteApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteRepository {
    private val quoteApiService: QuoteApiService

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://zenquotes.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        quoteApiService = retrofit.create(QuoteApiService::class.java)
    }

    suspend fun getDailyQuote() = quoteApiService.getDailyQuote()
} 