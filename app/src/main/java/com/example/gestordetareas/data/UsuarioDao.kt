package com.example.gestordetareas.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :usuario AND contrasena = :contrasena")
    suspend fun autenticar(usuario: String, contrasena: String): Usuario?
}
