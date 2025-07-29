package com.example.gestordetareas.ui

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Register : Screens("register")
    object Home : Screens("home")
    object ListaTareas : Screens("listaTareas")
    object CrearTarea : Screens("crearTarea")
    object EditarTarea : Screens("editar_tarea/{id}") {
        fun editarTareaConId(id: Int): String = "editar_tarea/$id"
    }
}




