package com.example.cinepass.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookingRepository

    var totalPrice = 0
    var selectedSeats: List<Int> = emptyList()

    init {
        val bookingDao = BookingDatabase.getDatabase(application).bookingDao()
        repository = BookingRepository(bookingDao)
        loadLastBooking()
    }

    fun saveBooking(price: Int, seats: List<Int>) {
        totalPrice = price
        selectedSeats = seats
        viewModelScope.launch {
            val seatsString = seats.joinToString(",")
            repository.insertBooking(BookingEntity(totalPrice = price, selectedSeats = seatsString))
        }
    }

    fun loadLastBooking() {
        viewModelScope.launch {
            val lastBooking = repository.getLastBooking()
            lastBooking?.let {
                totalPrice = it.totalPrice
                selectedSeats = it.selectedSeats.split(",").mapNotNull { seat -> seat.toIntOrNull() }
            }
        }
    }

    fun clearBooking() {
        totalPrice = 0
        selectedSeats = emptyList()
        viewModelScope.launch {
            repository.clearBookings()
        }
    }
}
