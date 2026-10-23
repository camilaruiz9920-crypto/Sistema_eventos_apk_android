package com.gestion.eventos.network

import com.gestion.eventos.model.AuthResponse
import com.gestion.eventos.model.Evento
import com.gestion.eventos.model.LoginRequest
import com.gestion.eventos.model.RegisterRequest
import retrofit2.Response
import java.io.IOException

/**
 * Repositorio: única fuente de datos para los ViewModels (capa Model de MVVM).
 *
 * Encapsula las llamadas Retrofit y traduce respuestas/errores HTTP a [Result],
 * de modo que la UI nunca maneja excepciones de red directamente.
 *
 * @param api implementación de [ApiService] (inyectable para pruebas).
 */
class EventoRepository(private val api: ApiService = RetrofitClient.api) {

    /** Inicia sesión contra `POST auth/login`. */
    suspend fun login(email: String, password: String): Result<AuthResponse> =
        ejecutar { api.login(LoginRequest(email, password)) }

    /** Registra un usuario contra `POST auth/register`. */
    suspend fun register(nombre: String, email: String, password: String): Result<AuthResponse> =
        ejecutar { api.register(RegisterRequest(nombre, email, password)) }

    /**
     * Obtiene el catálogo y descarta eventos con tarifa <= 0
     * (regla de negocio: solo se muestran tarifas reales).
     */
    suspend fun obtenerEventos(): Result<List<Evento>> =
        ejecutar { api.getEventos() }.map { lista -> lista.filter { it.tieneTarifaValida() } }

    /**
     * Ejecuta una llamada Retrofit y la envuelve en [Result].
     *
     * @param llamada bloque suspendido que devuelve una [Response].
     * @return `Result.success` con el cuerpo, o `Result.failure` con un mensaje legible.
     */
    private suspend fun <T> ejecutar(llamada: suspend () -> Response<T>): Result<T> {
        return try {
            val response = llamada()
            val body = response.body()
            when {
                response.isSuccessful && body != null -> Result.success(body)
                response.code() == 401 || response.code() == 403 ->
                    Result.failure(Exception("Credenciales inválidas o sesión expirada"))
                response.code() == 409 ->
                    Result.failure(Exception("El usuario ya existe"))
                else -> Result.failure(Exception("Error del servidor (${response.code()})"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar con el servidor. ¿Está corriendo Spring Boot?"))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.localizedMessage}"))
        }
    }
}
