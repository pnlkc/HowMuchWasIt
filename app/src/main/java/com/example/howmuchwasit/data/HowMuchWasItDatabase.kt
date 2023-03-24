package com.example.howmuchwasit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class HowMuchWasItDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: HowMuchWasItDatabase? = null

        fun getDatabase(context: Context): HowMuchWasItDatabase {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, HowMuchWasItDatabase::class.java, "products_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}