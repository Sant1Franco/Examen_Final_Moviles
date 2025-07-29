package com.example.gestordetareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestordetareas.data.Task
import com.example.gestordetareas.ui.Screens
import com.example.gestordetareas.ui.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearTareaScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var errorTitulo by remember { mutableStateOf("") }
    var errorFecha by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var estaGuardando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Tarea") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
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
                value = fecha,
                onValueChange = {
                    fecha = it
                    if (validarFecha(it)) errorFecha = "" else errorFecha = "Formato inválido o fecha pasada"
                },
                label = { Text("Fecha límite (dd-MM-yyyy)") },
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
                    var valido = true

                    if (titulo.trim().length < 3) {
                        errorTitulo = "El título debe tener al menos 3 caracteres"
                        valido = false
                    }
                    if (!validarFecha(fecha)) {
                        errorFecha = "La fecha debe tener formato válido y ser actual o futura"
                        valido = false
                    }

                    if (valido && !estaGuardando) {
                        estaGuardando = true
                        val nueva = Task(
                            titulo = titulo.trim(),
                            descripcion = descripcion.trim(),
                            fechaLimite = convertirFechaAISO(fecha)
                        )

                        viewModel.agregarTarea(nueva) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Tarea creada correctamente",
                                    duration = SnackbarDuration.Short
                                )
                                navController.navigate(Screens.Home.route) {
                                    popUpTo(Screens.Login.route) { inclusive = false }
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
                Text("Guardar")
            }
        }
    }
}

fun validarFecha(fecha: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        sdf.isLenient = false
        val ingresada = sdf.parse(fecha)
        val hoy = sdf.parse(sdf.format(Date()))

        ingresada != null && hoy != null &&
                (ingresada.after(hoy) || ingresada == hoy)
    } catch (e: Exception) {
        false
    }
}

fun convertirFechaAISO(fecha: String): String {
    return try {
        val entrada = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val salida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        salida.format(entrada.parse(fecha)!!)
    } catch (e: Exception) {
        fecha
    }
}

fun convertirFechaAUsuario(fechaISO: String): String {
    return try {
        val iso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val usuario = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        usuario.format(iso.parse(fechaISO)!!)
    } catch (e: Exception) {
        fechaISO
    }
}
