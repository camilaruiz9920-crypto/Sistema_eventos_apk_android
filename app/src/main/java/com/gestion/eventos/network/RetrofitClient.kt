package com.gestion.eventos.network

import com.gestion.eventos.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton que construye y expone la instancia única de Retrofit.
 *
 * - Usa Gson como conversor JSON.
 * - Añade el encabezado `Authorization: Bearer <token>` si hay sesión activa.
 * - Registra en Logcat las peticiones/respuestas en builds de depuración.
 */
object RetrofitClient {

    /** Interceptor que adjunta el token JWT guardado en [SessionManager]. */
    private val authInterceptor = Interceptor { chain ->
        val token = SessionManager.token
        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }
        chain.proceed(request)
    }

    /** Interceptor de logging (solo cuerpo completo en DEBUG). */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /** Implementación perezosa de [ApiService] lista para usarse desde los repositorios. */
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // http://10.0.2.2:8080/api/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
