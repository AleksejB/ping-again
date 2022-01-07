package com.thestart.pingagain.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thestart.pingagain.data.model.Host

@Database(entities = [Host::class], version = 2, exportSchema = false)
abstract class HostDatabase: RoomDatabase() {
    abstract fun hostsDao(): HostDao

    companion object {
        @Volatile
        private var INSTANCE: HostDatabase? = null

        fun getDatabase(context: Context): HostDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    HostDatabase::class.java,
                    "hosts.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}