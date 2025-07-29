package com.example.gestordetareas.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.gestordetareas.ui.TaskViewModel
import com.example.gestordetareas.ui.UsuarioViewModel
import com.example.gestordetareas.ui.AppNavHost
import com.example.gestordetareas.ui.theme.GestorDeTareasTheme

@Composable
fun GestorDeTareasApp(
    taskViewModel: TaskViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val navController = rememberNavController()

    GestorDeTareasTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        AppNavHost(
            navController = navController,
            taskViewModel = taskViewModel,
            usuarioViewModel = usuarioViewModel
        )
    }
}