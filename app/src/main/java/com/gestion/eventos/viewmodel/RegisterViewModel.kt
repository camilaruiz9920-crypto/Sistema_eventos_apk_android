package com.gestion.eventos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestion.eventos.model.AuthResponse
import com.gestion.eventos.network.EventoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de registro.
 *
 * Aplica la validación de campos vacíos exigida por la rúbrica, el formato
 * de correo, longitud mínima de contraseña y coincidencia de confirmación.
 */
class RegisterViewModel(
    private val repository: EventoRepository = EventoRepository()
) : ViewModel() {

    private val _estado = MutableLiveData<UiState<AuthResponse>>(UiState.Idle)

    /** Estado observable por [com.gestion.eventos.ui.RegisterActivity]. */
    val estado: LiveData<UiState<AuthResponse>> = _estado

    /**
     * Registra un nuevo usuario si los datos son válidos.
     */
    fun registrar(nombre: String, email: String, password: String, confirmacion: String) {
        val error = validar(nombre, email, password, confirmacion)
        if (error != null) {
            _estado.value = UiState.Error(error)
            return
        }
        _estado.value = UiState.Loading
        viewModelScope.launch {
            repository.register(nombre.trim(), email.trim(), password)
                .onSuccess { _estado.value = UiState.Success(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "Error en el registro") }
        }
    }

    /** @return mensaje de error de validación o `null` si todo es correcto. */
    private fun validar(nombre: String, email: String, password: String, confirmacion: String): String? =
        when {
            nombre.isBlank() || email.isBlank() || password.isBlank() || confirmacion.isBlank() ->
                "Todos los campos son obligatorios"
            !LoginViewModel.EMAIL_REGEX.matches(email.trim()) -> "Correo electrónico no válido"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            password != confirmacion -> "Las contraseñas no coinciden"
            else -> null
        }
}
