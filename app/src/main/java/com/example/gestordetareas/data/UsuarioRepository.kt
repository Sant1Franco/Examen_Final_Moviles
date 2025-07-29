package com.example.gestordetareas.data

import java.security.MessageDigest

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun insertar(usuario: Usuario) {
        val usuarioHasheado = usuario.copy(
            contrasena = hashContrasena(usuario.contrasena)
        )
        dao.insertar(usuarioHasheado)
    }

    suspend fun autenticar(usuario: String, contrasena: String): Usuario? {
        val hash = hashContrasena(contrasena)
        return dao.autenticar(usuario, hash)
    }

    private fun hashContrasena(contrasena: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(contrasena.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
