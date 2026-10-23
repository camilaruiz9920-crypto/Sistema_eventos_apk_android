package com.gestion.eventos.network

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestiona la sesión del usuario (token JWT) usando SharedPreferences.
 *
 * Debe inicializarse una vez con [init] (se hace en LoginActivity).
 */
object SessionManager {

    private const val PREFS = "gestion_eventos_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_NOMBRE = "nombre"

    private var prefs: SharedPreferences? = null

    /** Inicializa las preferencias con el contexto de aplicación. */
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
    }

    /** Token JWT actual o `null` si no hay sesión. */
    val token: String?
        get() = prefs?.getString(KEY_TOKEN, null)

    /** Nombre del usuario autenticado (para saludo en el catálogo). */
    val nombreUsuario: String?
        get() = prefs?.getString(KEY_NOMBRE, null)

    /** Guarda los datos de sesión tras un login exitoso. */
    fun guardarSesion(token: String?, nombre: String?) {
        prefs?.edit()
            ?.putString(KEY_TOKEN, token)
            ?.putString(KEY_NOMBRE, nombre)
            ?.apply()
    }

    /** Elimina la sesión (logout). */
    fun cerrarSesion() {
        prefs?.edit()?.clear()?.apply()
    }
}
