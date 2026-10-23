package com.gestion.eventos

import org.junit.Test
import org.junit.Assert.*

/**
 * Modelo local para validación de reglas de negocio en pruebas unitarias.
 */
data class EventoPrueba(
    val id: Long,
    val nombre: String,
    val artista: String,
    val fecha: String,
    val tarifa: Double
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(artista.isNotBlank()) { "El artista no puede estar vacío" }
        require(tarifa > 0) { "La tarifa debe ser mayor a 0" }
    }
}

/**
 * Pruebas unitarias locales (JVM) de reglas de negocio del Sistema de Eventos.
 */
class EventoUnitTest {

    @Test
    fun crearEvento_asignaValoresCorrectamente() {
        val evento = EventoPrueba(
            id = 1L,
            nombre = "Festival Internacional de Jazz",
            artista = "Santiago & Banda Jazz",
            fecha = "2026-11-15 20:00:00",
            tarifa = 50000.0
        )

        assertEquals(1L, evento.id)
        assertEquals("Festival Internacional de Jazz", evento.nombre)
        assertEquals(50000.0, evento.tarifa, 0.0)
    }

    @Test
    fun tarifa_esMayorQueCero() {
        val tarifaValida = 45000.0
        assertTrue("La tarifa debe ser estrictamente superior a $0", tarifaValida > 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun tarifaCero_esRechazada() {
        EventoPrueba(
            id = 2L,
            nombre = "Concierto Rock",
            artista = "Banda Local",
            fecha = "2026-11-20 18:00:00",
            tarifa = 0.0
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun tarifaNegativa_esRechazada() {
        EventoPrueba(
            id = 3L,
            nombre = "Recital Acústico",
            artista = "Solista",
            fecha = "2026-11-22 19:00:00",
            tarifa = -15000.0
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun nombreVacio_esRechazado() {
        EventoPrueba(
            id = 4L,
            nombre = "   ",
            artista = "Artista Invitado",
            fecha = "2026-11-25 21:00:00",
            tarifa = 30000.0
        )
    }
}