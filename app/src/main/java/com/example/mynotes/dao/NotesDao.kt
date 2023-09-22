package com.example.mynotes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mynotes.entities.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes ORDER BY id DESC")
    fun getAll(): List<Notes>

    @Insert()
    fun insertNote(note: Notes)

    @Delete
    fun deleteNote(note: Notes)

    @Update
    fun upDataNote(note: Notes)

    @Query("SELECT title FROM Notes")
    fun getAllTitle(): List<String>
}