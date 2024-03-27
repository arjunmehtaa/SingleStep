package com.example.singlestep.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.singlestep.model.RoomTripSummary

@Database(entities = [RoomTripSummary::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripSummaryDao(): TripSummaryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "trips"
            ).fallbackToDestructiveMigration().build()
    }

}