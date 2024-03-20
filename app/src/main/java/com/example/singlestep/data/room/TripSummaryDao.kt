package com.example.singlestep.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.singlestep.model.RoomTripSummary

@Dao
interface TripSummaryDao {
    @Query("SELECT * FROM roomtripsummary")
    fun getAll(): List<RoomTripSummary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tripSummaries: RoomTripSummary)

    @Delete
    fun delete(roomTripSummary: RoomTripSummary)
}