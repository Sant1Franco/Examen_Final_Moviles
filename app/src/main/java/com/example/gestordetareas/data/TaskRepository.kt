package com.example.gestordetareas.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    suspend fun insertar(task: Task) = dao.insertar(task)
    suspend fun actualizar(task: Task) = dao.actualizar(task)
    suspend fun eliminar(task: Task) = dao.eliminar(task)
    fun obtenerTodas(): Flow<List<Task>> = dao.obtenerTodas()
    suspend fun obtenerPorId(id: Int): Task? = dao.obtenerPorId(id)
}
