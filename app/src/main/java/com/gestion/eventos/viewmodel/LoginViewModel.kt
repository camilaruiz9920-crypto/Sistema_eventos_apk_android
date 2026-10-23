package com.gestion.eventos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestion.eventos.model.AuthResponse
import com.gestion.eventos.network.EventoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de inicio de sesión.
 *
 * Valida las credenciales localmente y, si son correctas, delega la
 * autenticación al [EventoRepository]. Sobrevive a rotaciones de pantalla.
 */
class LoginViewModel(
    private val repository: EventoRepository = EventoRepository()
) : ViewModel() {

    private val _estado = MutableLiveData<UiState<AuthResponse>>(UiState.Idle)

    /** Estado observable por [com.gestion.eventos.ui.LoginActivity]. */
    val estado: LiveData<UiState<AuthResponse>> = _estado

    /**
     * Intenta iniciar sesión.
     * @param email correo ingresado.
     * @param password contraseña ingresada.
     */
    fun login(email: String, password: String) {
        val error = validar(email, password)
        if (error != null) {
            _estado.value = UiState.Error(error)
            return
        }
        _estado.value = UiState.Loading
        viewModelScope.launch {
            repository.login(email.trim(), password)
                .onSuccess { _estado.value = UiState.Success(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "Error al iniciar sesión") }
        }
    }

    /** Valida campos vacíos y formato de correo. @return mensaje de error o `null`. */
    private fun validar(email: String, password: String): String? = when {
        email.isBlank() || password.isBlank() -> "Todos los campos son obligatorios"
        !EMAIL_REGEX.matches(email.trim()) -> "Correo electrónico no válido"
        else -> null
    }

    companion object {
        /** Expresión regular simple para validar correos (independiente de Android). */
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
