package com.example.cinepass.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingRepository(private val bookingDao: BookingDao) {

    suspend fun insertBooking(booking: BookingEntity) {
        withContext(Dispatchers.IO) {
            bookingDao.insertBooking(booking)
        }
    }

    suspend fun getLastBooking(): BookingEntity? {
        return withContext(Dispatchers.IO) {
            bookingDao.getLastBooking()
        }
    }

    suspend fun clearBookings() {
        withContext(Dispatchers.IO) {
            bookingDao.clearBookings()
        }
    }
}
