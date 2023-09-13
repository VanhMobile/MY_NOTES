package com.example.mynotes.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.dao.NotesDao
import com.example.mynotes.entities.Notes

@Database(entities = [Notes::class], version = 1)
abstract class NotesDataBase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: NotesDataBase? = null

        fun getDatabase(context: Context): NotesDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        NotesDataBase::class.java,
                        "Database.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                INSTANCE = instance
                return instance
            }
        }

    }

    abstract fun noteDao(): NotesDao
}