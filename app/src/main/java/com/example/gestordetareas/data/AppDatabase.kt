package com.example.gestordetareas.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class, Usuario::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun usuarioDao(): UsuarioDao
}
