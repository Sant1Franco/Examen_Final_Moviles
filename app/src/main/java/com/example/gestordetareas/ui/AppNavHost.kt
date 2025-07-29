package com.example.gestordetareas.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.gestordetareas.ui.screens.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    NavHost(navController, startDestination = Screens.Login.route) {
        composable(Screens.Login.route) {
            LoginScreen(navController, usuarioViewModel)
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController, usuarioViewModel)
        }
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(Screens.ListaTareas.route) {
            ListaTareasScreen(navController, taskViewModel)
        }
        composable(Screens.CrearTarea.route) {
            CrearTareaScreen(navController, taskViewModel)
        }
        composable(
            route = Screens.EditarTarea.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            EditarTareaScreen(
                navController = navController,
                taskViewModel = taskViewModel,
                tareaId = id
            )
        }
    }
}



