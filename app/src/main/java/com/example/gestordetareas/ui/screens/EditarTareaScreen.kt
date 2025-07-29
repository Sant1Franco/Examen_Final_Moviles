package com.example.gestordetareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestordetareas.data.Task
import com.example.gestordetareas.ui.Screens
import com.example.gestordetareas.ui.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarTareaScreen(
    navController: NavController,
    taskViewModel: TaskViewModel,
    tareaId: Int
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var tarea by remember { mutableStateOf<Task?>(null) }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaLimite by remember { mutableStateOf("") }

    var errorTitulo by remember { mutableStateOf("") }
    var errorFecha by remember { mutableStateOf("") }
    var estaGuardando by remember { mutableStateOf(false) }
    var mostrarConfirmacionEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(tareaId) {
        taskViewModel.obtenerTareaPorId(tareaId) { tareaEncontrada ->
            tarea = tareaEncontrada
            titulo = tareaEncontrada?.titulo ?: ""
            descripcion = tareaEncontrada?.descripcion ?: ""
            fechaLimite = tareaEncontrada?.fechaLimite?.let { convertirFechaAUsuario(it) } ?: ""
        }
    }

    if (tarea == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Tarea") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->

        if (mostrarConfirmacionEliminar) {
            AlertDialog(
                onDismissRequest = { mostrarConfirmacionEliminar = false },
                title = { Text("¿Eliminar Tarea?") },
                text = { Text("¿Estás seguro de que deseas eliminar esta tarea? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarConfirmacionEliminar = false
                        tarea?.let {
                            taskViewModel.eliminarTarea(it) {
                                scope.launch {
                                    val job = launch {
                                        snackbarHostState.showSnackbar("Tarea eliminada con éxito")
                                    }
                                    kotlinx.coroutines.delay(900)
                                    job.cancel()

                                    navController.navigate(Screens.ListaTareas.route) {
                                        popUpTo(Screens.Home.route) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarConfirmacionEliminar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    if (it.trim().length >= 3) errorTitulo = ""
                },
                label = { Text("Título") },
                isError = errorTitulo.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (errorTitulo.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    errorTitulo,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fechaLimite,
                onValueChange = {
                    fechaLimite = it
                    if (validarFecha(it)) errorFecha = "" else errorFecha = "Formato inválido o fecha pasada"
                },
                label = { Text("Fecha Límite (dd-MM-yyyy)") },
                isError = errorFecha.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (errorFecha.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    errorFecha,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val tituloValido = titulo.trim().length >= 3
                    val fechaValida = validarFecha(fechaLimite)

                    if (!tituloValido) errorTitulo = "El título debe tener al menos 3 caracteres"
                    if (!fechaValida) errorFecha = "La fecha debe tener formato válido y ser actual o futura"

                    if (tituloValido && fechaValida && !estaGuardando) {
                        estaGuardando = true
                        taskViewModel.actualizarTarea(
                            Task(
                                id = tareaId,
                                titulo = titulo.trim(),
                                descripcion = descripcion.trim().ifBlank { null },
                                fechaLimite = convertirFechaAISO(fechaLimite)
                            )
                        ) {
                            scope.launch {
                                val job = launch {
                                    snackbarHostState.showSnackbar("Cambios guardados con éxito")
                                }
                                kotlinx.coroutines.delay(2000)
                                job.cancel()

                                navController.navigate(Screens.ListaTareas.route) {
                                    popUpTo(Screens.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !estaGuardando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Guardar Cambios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { mostrarConfirmacionEliminar = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar Tarea")
            }
        }
    }
}