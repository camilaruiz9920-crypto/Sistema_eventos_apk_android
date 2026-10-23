package com.gestion.eventos.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gestion.eventos.R
import com.gestion.eventos.databinding.ActivityCatalogBinding
import com.gestion.eventos.network.SessionManager
import com.gestion.eventos.viewmodel.CatalogViewModel
import com.gestion.eventos.viewmodel.UiState
import com.google.android.material.snackbar.Snackbar

/**
 * Pantalla principal: catálogo de eventos en un RecyclerView.
 *
 * Muestra nombre, artista, fecha y tarifa (> $0) de cada evento obtenido
 * del backend. Soporta pull-to-refresh y cierre de sesión.
 */
class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private val viewModel: CatalogViewModel by viewModels()
    private val adapter = EventoAdapter()

    /** Inicializa toolbar, RecyclerView, refresco y observadores. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarToolbar()

        binding.rvEventos.layoutManager = LinearLayoutManager(this)
        binding.rvEventos.adapter = adapter
        binding.rvEventos.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener { viewModel.cargarEventos() }
        binding.btnReintentar.setOnClickListener { viewModel.cargarEventos() }

        observarEventos()
    }

    /** Muestra saludo al usuario y gestiona el menú de cerrar sesión. */
    private fun configurarToolbar() {
        SessionManager.nombreUsuario?.let { binding.toolbar.subtitle = "Hola, $it" }
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_logout) {
                SessionManager.cerrarSesion()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            } else false
        }
    }

    /** Observa el estado del catálogo y actualiza lista, carga y vista vacía/error. */
    private fun observarEventos() {
        viewModel.eventos.observe(this) { estado ->
            binding.swipeRefresh.isRefreshing = estado is UiState.Loading
            when (estado) {
                is UiState.Success -> {
                    adapter.submitList(estado.data)
                    val vacio = estado.data.isEmpty()
                    binding.layoutEstado.visibility = if (vacio) View.VISIBLE else View.GONE
                    binding.tvEstado.text = getString(R.string.catalogo_vacio)
                    binding.btnReintentar.visibility = View.GONE
                    binding.tvContador.text = getString(R.string.contador_eventos, estado.data.size)
                }
                is UiState.Error -> {
                    binding.layoutEstado.visibility =
                        if (adapter.itemCount == 0) View.VISIBLE else View.GONE
                    binding.tvEstado.text = estado.mensaje
                    binding.btnReintentar.visibility = View.VISIBLE
                    Snackbar.make(binding.root, estado.mensaje, Snackbar.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }
    }
}
