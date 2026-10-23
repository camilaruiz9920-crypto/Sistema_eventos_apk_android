package com.gestion.eventos.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de dominio que representa un evento del catálogo.
 *
 * Se mapea directamente desde el JSON que devuelve el backend Spring Boot
 * (`GET /api/eventos`) mediante Gson.
 *
 * @property id Identificador único del evento en la base de datos MySQL.
 * @property nombre Nombre comercial del evento.
 * @property artista Artista o agrupación principal.
 * @property fecha Fecha del evento en formato ISO `yyyy-MM-dd` (tal como la serializa Spring).
 * @property tarifa Precio real de la entrada. Debe ser estrictamente mayor que 0.
 */
data class Evento(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("artista") val artista: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("tarifa") val tarifa: Double
) {

    /**
     * Indica si la tarifa del evento es válida para mostrarse en el catálogo.
     *
     * Nota: Gson no invoca el constructor de Kotlin al deserializar, por eso
     * la validación se expone como función y se aplica también en el Repository.
     *
     * @return `true` si la tarifa es mayor que 0.
     */
    fun tieneTarifaValida(): Boolean = tarifa > 0.0

    companion object {

        /**
         * Fábrica que crea un [Evento] validando sus reglas de negocio.
         *
         * @throws IllegalArgumentException si algún texto está vacío o la tarifa es <= 0.
         */
        fun crear(
            id: Long,
            nombre: String,
            artista: String,
            fecha: String,
            tarifa: Double
        ): Evento {
            require(nombre.isNotBlank()) { "El nombre del evento es obligatorio" }
            require(artista.isNotBlank()) { "El artista es obligatorio" }
            require(fecha.isNotBlank()) { "La fecha es obligatoria" }
            require(tarifa > 0.0) { "La tarifa debe ser mayor a 0" }
            return Evento(id, nombre.trim(), artista.trim(), fecha.trim(), tarifa)
        }
    }
}
