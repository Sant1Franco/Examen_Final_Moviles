package com.example.gestordetareas.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestordetareas.data.Task
import com.example.gestordetareas.ui.Screens
import com.example.gestordetareas.ui.TaskUiState
import com.example.gestordetareas.ui.TaskViewModel
import androidx.compose.runtime.mutableIntStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.cargarTareas()
    }

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas Registradas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.CrearTarea.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva tarea")
            }
        }
    ) { padding ->
        when (uiState) {
            is TaskUiState.Cargando -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TaskUiState.Exito -> {
                val tareasPendientes = uiState.tareas.filter { !it.completada && !it.vencida }
                val tareasVencidas = uiState.tareas.filter { it.vencida }
                val tareasHechas = uiState.tareas.filter { it.completada }

                if (uiState.tareas.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No hay tareas registradas aún",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        if (tareasPendientes.isNotEmpty()) {
                            item {
                                Text(
                                    "Pendientes",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(tareasPendientes) { tarea ->
                                TareaItem(tarea, navController, viewModel)
                            }

                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        if (tareasVencidas.isNotEmpty()) {
                            item {
                                Text(
                                    "Vencidas",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(tareasVencidas) { tarea ->
                                TareaItem(tarea, navController, viewModel)
                            }

                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        if (tareasHechas.isNotEmpty()) {
                            item {
                                Text(
                                    "Completadas",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(tareasHechas) { tarea ->
                                TareaItem(tarea, navController, viewModel)
                            }
                        }
                    }
                }
            }

            is TaskUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${uiState.mensaje}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun TareaItem(tarea: Task, navController: NavController, viewModel: TaskViewModel) {
    var expandida by remember { mutableStateOf(false) }
    var mostrarVerMas by remember { mutableStateOf(false) }
    val alphaTexto = if (tarea.completada) 0.5f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { completado ->
                    viewModel.cambiarEstadoTarea(tarea, completado)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screens.EditarTarea.editarTareaConId(tarea.id))
                    }
                    .weight(1f)
            ) {
                Text(
                    tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else null,
                    modifier = Modifier.alpha(alphaTexto)
                )

                if (!tarea.descripcion.isNullOrBlank()) {
                    var lineCount by remember { mutableIntStateOf(0) }

                    Text(
                        text = tarea.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        onTextLayout = { result ->
                            lineCount = result.lineCount
                            mostrarVerMas = lineCount > 2
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.dp),
                        maxLines = Int.MAX_VALUE
                    )

                    Text(
                        text = tarea.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (expandida) Int.MAX_VALUE else 2,
                        textDecoration = if (tarea.completada) TextDecoration.LineThrough else null,
                        modifier = Modifier.alpha(alphaTexto)
                    )

                    if (mostrarVerMas) {
                        Text(
                            text = if (expandida) "Ver menos" else "Ver más",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .clickable { expandida = !expandida }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Vence: ${convertirFechaAUsuario(tarea.fechaLimite)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.alpha(alphaTexto)
                )
            }
        }
    }
}
