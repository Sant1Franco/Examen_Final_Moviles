package com.example.gestordetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.gestordetareas.data.AppDatabase
import com.example.gestordetareas.data.TaskRepository
import com.example.gestordetareas.data.UsuarioRepository
import com.example.gestordetareas.ui.TaskViewModel
import com.example.gestordetareas.ui.UsuarioViewModel
import com.example.gestordetareas.ui.screens.GestorDeTareasApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tareas_db"
        )
            .build()

        val taskRepository = TaskRepository(db.taskDao())
        val usuarioRepository = UsuarioRepository(db.usuarioDao())

        val taskViewModel = TaskViewModel(taskRepository)
        val usuarioViewModel = UsuarioViewModel(usuarioRepository)

        setContent {
            GestorDeTareasApp(
                taskViewModel = taskViewModel,
                usuarioViewModel = usuarioViewModel
            )
        }
    }
}
