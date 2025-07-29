package com.example.gestordetareas.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insertar(task: Task)

    @Update
    suspend fun actualizar(task: Task)

    @Delete
    suspend fun eliminar(task: Task)

    @Query("SELECT * FROM Task ORDER BY fechaLimite ASC")
    fun obtenerTodas(): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Task?
}
