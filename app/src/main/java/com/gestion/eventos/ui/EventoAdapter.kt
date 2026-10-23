package com.gestion.eventos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gestion.eventos.databinding.ItemEventoBinding
import com.gestion.eventos.model.Evento
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter del RecyclerView del catálogo.
 *
 * Usa [ListAdapter] + [DiffUtil] para actualizar solo los ítems que cambian,
 * lo que mejora el rendimiento frente a `notifyDataSetChanged()`.
 */
class EventoAdapter : ListAdapter<Evento, EventoAdapter.EventoViewHolder>(DIFF) {

    /** Crea el ViewHolder inflando `item_evento.xml` con ViewBinding. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventoViewHolder(binding)
    }

    /** Vincula el evento de la posición indicada con su vista. */
    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder que pinta nombre, artista, fecha y tarifa de un [Evento].
     */
    class EventoViewHolder(private val binding: ItemEventoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /** Asigna los datos del evento a los TextViews. */
        fun bind(evento: Evento) {
            binding.tvNombre.text = evento.nombre
            binding.tvArtista.text = evento.artista
            binding.tvFecha.text = formatearFecha(evento.fecha)
            binding.tvTarifa.text = MONEDA.format(evento.tarifa)
        }
    }

    companion object {
        /** Formato de moneda colombiana (ej. $ 150.000). */
        private val MONEDA: NumberFormat =
            NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply { maximumFractionDigits = 0 }

        private val ENTRADA = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val SALIDA = SimpleDateFormat("dd MMM yyyy", Locale("es", "CO"))

        /**
         * Convierte `yyyy-MM-dd` (o `yyyy-MM-ddTHH:mm:ss`) a `dd MMM yyyy`.
         * Si el formato no es reconocido devuelve el texto original.
         */
        fun formatearFecha(fecha: String): String = try {
            ENTRADA.parse(fecha.take(10))?.let { SALIDA.format(it) } ?: fecha
        } catch (e: Exception) {
            fecha
        }

        /** Compara eventos por id (mismo ítem) y por contenido (mismos datos). */
        private val DIFF = object : DiffUtil.ItemCallback<Evento>() {
            override fun areItemsTheSame(old: Evento, new: Evento) = old.id == new.id
            override fun areContentsTheSame(old: Evento, new: Evento) = old == new
        }
    }
}
