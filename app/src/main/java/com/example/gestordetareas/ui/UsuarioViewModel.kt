package com.example.gestordetareas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestordetareas.data.Usuario
import com.example.gestordetareas.data.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repo: UsuarioRepository) : ViewModel() {
    suspend fun autenticar(usuario: String, contrasena: String): Boolean {
        return repo.autenticar(usuario, contrasena) != null
    }

    fun registrar(usuario: String, contrasena: String) {
        viewModelScope.launch {
            repo.insertar(Usuario(nombreUsuario = usuario, contrasena = contrasena))
        }
    }
}
