package com.princemaurya.robokalam.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolios")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val college: String,
    val skills: String,
    val projectTitle: String,
    val projectDescription: String,
    val createdAt: Long = System.currentTimeMillis()
) 