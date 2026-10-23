package com.gestion.eventos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestion.eventos.model.Evento
import com.gestion.eventos.network.EventoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel del catálogo de eventos.
 *
 * Carga la lista desde el backend al crearse y permite recargarla
 * (pull-to-refresh). Los datos se conservan ante cambios de configuración.
 */
class CatalogViewModel(
    private val repository: EventoRepository = EventoRepository()
) : ViewModel() {

    private val _eventos = MutableLiveData<UiState<List<Evento>>>(UiState.Idle)

    /** Lista de eventos observable por [com.gestion.eventos.ui.CatalogActivity]. */
    val eventos: LiveData<UiState<List<Evento>>> = _eventos

    init {
        cargarEventos()
    }

    /** Solicita el catálogo al repositorio y publica el estado resultante. */
    fun cargarEventos() {
        _eventos.value = UiState.Loading
        viewModelScope.launch {
            repository.obtenerEventos()
                .onSuccess { _eventos.value = UiState.Success(it) }
                .onFailure { _eventos.value = UiState.Error(it.message ?: "No se pudo cargar el catálogo") }
        }
    }
}
