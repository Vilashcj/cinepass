package com.example.cinepass.room

import androidx.room.*

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM booking_table ORDER BY id DESC LIMIT 1")
    suspend fun getLastBooking(): BookingEntity?

    @Query("DELETE FROM booking_table")
    suspend fun clearBookings()
}
