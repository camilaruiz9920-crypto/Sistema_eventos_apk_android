package com.gestion.eventos.model

import com.google.gson.annotations.SerializedName

/**
 * Cuerpo (JSON) enviado a `POST /api/auth/login`.
 *
 * @property email Correo del usuario registrado.
 * @property password Contraseña en texto plano (viaja dentro del cuerpo de la petición).
 */
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/**
 * Cuerpo (JSON) enviado a `POST /api/auth/register`.
 *
 * @property nombre Nombre completo del nuevo usuario.
 * @property email Correo electrónico (único en la BD).
 * @property password Contraseña elegida.
 */
data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/**
 * Respuesta de los endpoints de autenticación.
 *
 * Todos los campos son opcionales para tolerar variaciones del backend
 * (p. ej. el registro puede no devolver token).
 *
 * @property token Token JWT (si el backend lo emite).
 * @property mensaje Mensaje informativo del servidor.
 * @property usuario Datos básicos del usuario autenticado.
 */
data class AuthResponse(
    @SerializedName("token") val token: String? = null,
    @SerializedName("mensaje") val mensaje: String? = null,
    @SerializedName("usuario") val usuario: Usuario? = null
)

/**
 * Representación del usuario devuelta por el backend.
 */
data class Usuario(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("email") val email: String? = null
)
