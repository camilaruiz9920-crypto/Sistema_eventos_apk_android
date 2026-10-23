package com.gestion.eventos.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gestion.eventos.databinding.ActivityLoginBinding
import com.gestion.eventos.network.SessionManager
import com.gestion.eventos.viewmodel.LoginViewModel
import com.gestion.eventos.viewmodel.UiState
import com.google.android.material.snackbar.Snackbar

/**
 * Pantalla de inicio de sesión (punto de entrada de la app).
 *
 * Solo se encarga de capturar eventos de la UI y observar el [LoginViewModel];
 * no contiene lógica de negocio ni llamadas de red (MVVM).
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    /** Infla la vista, verifica sesión previa y registra listeners/observadores. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)

        // Si ya existe una sesión, se salta directamente al catálogo.
        if (!SessionManager.token.isNullOrBlank()) {
            irAlCatalogo()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text?.toString().orEmpty(),
                binding.etPassword.text?.toString().orEmpty()
            )
        }
        binding.tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        observarEstado()
    }

    /** Renderiza la UI según el [UiState] emitido por el ViewModel. */
    private fun observarEstado() {
        viewModel.estado.observe(this) { estado ->
            val cargando = estado is UiState.Loading
            binding.progress.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !cargando

            when (estado) {
                is UiState.Success -> {
                    SessionManager.guardarSesion(
                        token = estado.data.token ?: "sesion-activa",
                        nombre = estado.data.usuario?.nombre
                    )
                    irAlCatalogo()
                }
                is UiState.Error -> Snackbar.make(binding.root, estado.mensaje, Snackbar.LENGTH_LONG).show()
                else -> Unit
            }
        }
    }

    /** Navega al catálogo y elimina el login de la pila de navegación. */
    private fun irAlCatalogo() {
        startActivity(Intent(this, CatalogActivity::class.java))
        finish()
    }
}
