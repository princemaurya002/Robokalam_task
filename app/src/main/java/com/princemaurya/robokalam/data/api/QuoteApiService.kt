package com.princemaurya.robokalam.data.api

import com.princemaurya.robokalam.data.model.Quote
import retrofit2.http.GET

interface QuoteApiService {
    @GET("api/today")
    suspend fun getDailyQuote(): List<Quote>
} 