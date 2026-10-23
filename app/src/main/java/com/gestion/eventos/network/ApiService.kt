package com.gestion.eventos.network

import com.gestion.eventos.model.AuthResponse
import com.gestion.eventos.model.Evento
import com.gestion.eventos.model.LoginRequest
import com.gestion.eventos.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Contrato Retrofit de la API REST del backend Spring Boot.
 *
 * Las rutas son relativas a `BuildConfig.BASE_URL` (`http://10.0.2.2:8080/api/`).
 * Si tus controladores usan otras rutas, ajústalas aquí únicamente.
 */
interface ApiService {

    /**
     * Autentica a un usuario.
     * @param request credenciales (email y password).
     * @return [AuthResponse] con el token/mensaje del servidor.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    /**
     * Registra un nuevo usuario en la base de datos.
     * @param request datos del usuario a crear.
     */
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    /**
     * Obtiene el catálogo completo de eventos.
     * @return lista de [Evento].
     */
    @GET("eventos")
    suspend fun getEventos(): Response<List<Evento>>
}
