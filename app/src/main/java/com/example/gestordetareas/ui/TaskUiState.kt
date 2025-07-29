package com.example.gestordetareas.ui

import com.example.gestordetareas.data.Task

sealed class TaskUiState {
    object Cargando : TaskUiState()
    data class Exito(val tareas: List<Task>) : TaskUiState()
    data class Error(val mensaje: String) : TaskUiState()
}
