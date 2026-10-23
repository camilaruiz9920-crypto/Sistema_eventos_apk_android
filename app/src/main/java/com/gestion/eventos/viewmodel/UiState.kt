package com.gestion.eventos.viewmodel

/**
 * Estado genérico de la UI expuesto por los ViewModels mediante LiveData.
 *
 * La vista solo observa este estado y se renderiza en consecuencia (MVVM unidireccional).
 */
sealed class UiState<out T> {
    /** Estado inicial, sin operación en curso. */
    data object Idle : UiState<Nothing>()

    /** Petición en curso: la vista debe mostrar un indicador de carga. */
    data object Loading : UiState<Nothing>()

    /** Operación exitosa con su resultado. */
    data class Success<T>(val data: T) : UiState<T>()

    /** Operación fallida con mensaje para el usuario. */
    data class Error(val mensaje: String) : UiState<Nothing>()
}
