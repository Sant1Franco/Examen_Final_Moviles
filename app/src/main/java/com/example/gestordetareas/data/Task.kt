package com.example.gestordetareas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String?,
    val fechaLimite: String,
    val completada: Boolean = false,
    val vencida: Boolean = false
)

