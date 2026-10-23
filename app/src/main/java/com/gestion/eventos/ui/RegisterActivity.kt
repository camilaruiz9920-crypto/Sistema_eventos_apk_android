package com.gestion.eventos.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gestion.eventos.databinding.ActivityRegisterBinding
import com.gestion.eventos.viewmodel.RegisterViewModel
import com.gestion.eventos.viewmodel.UiState
import com.google.android.material.snackbar.Snackbar

/**
 * Pantalla de registro de nuevos usuarios.
 *
 * Envía los datos al [RegisterViewModel], que valida campos vacíos antes
 * de llamar al endpoint `POST /api/auth/register`.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    /** Configura la toolbar, el botón de registro y el observador de estado. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnRegistrar.setOnClickListener {
            viewModel.registrar(
                nombre = binding.etNombre.text?.toString().orEmpty(),
                email = binding.etEmail.text?.toString().orEmpty(),
                password = binding.etPassword.text?.toString().orEmpty(),
                confirmacion = binding.etConfirmar.text?.toString().orEmpty()
            )
        }

        observarEstado()
    }

    /** Reacciona a los cambios de estado del registro. */
    private fun observarEstado() {
        viewModel.estado.observe(this) { estado ->
            val cargando = estado is UiState.Loading
            binding.progress.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnRegistrar.isEnabled = !cargando

            when (estado) {
                is UiState.Success -> {
                    Toast.makeText(this, "Registro exitoso. Inicia sesión.", Toast.LENGTH_LONG).show()
                    finish() // vuelve al Login
                }
                is UiState.Error -> Snackbar.make(binding.root, estado.mensaje, Snackbar.LENGTH_LONG).show()
                else -> Unit
            }
        }
    }
}
