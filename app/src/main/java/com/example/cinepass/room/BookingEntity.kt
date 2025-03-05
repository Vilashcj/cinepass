package com.example.cinepass.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking_table")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val totalPrice: Int,
    val selectedSeats: String // Store seats as a comma-separated string
)