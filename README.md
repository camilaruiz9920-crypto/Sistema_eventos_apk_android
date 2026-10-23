# 🎟️ Sistema de Gestión de Eventos — App Android

Cliente móvil nativo (Kotlin + XML) que consume la API REST del backend Spring Boot / MySQL.
Evidencia: **GA8-220501096-AA2-EV02**.

## Arquitectura (MVVM)

```
View (Activity + XML)  →  ViewModel (LiveData/UiState)  →  Repository  →  Retrofit (ApiService)  →  Spring Boot API
```

## Estructura de carpetas

```
GestionEventos/
├── .gitignore
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/gestion/eventos/
        │   │   ├── model/       Evento.kt, AuthModels.kt
        │   │   ├── network/     ApiService.kt, RetrofitClient.kt, EventoRepository.kt, SessionManager.kt
        │   │   ├── viewmodel/   UiState.kt, LoginViewModel.kt, RegisterViewModel.kt, CatalogViewModel.kt
        │   │   └── ui/          LoginActivity.kt, RegisterActivity.kt, CatalogActivity.kt, EventoAdapter.kt
        │   └── res/
        │       ├── layout/      activity_login.xml, activity_register.xml, activity_catalog.xml, item_evento.xml
        │       ├── values/      colors.xml, strings.xml, themes.xml
        │       ├── drawable/    bg_gradient.xml
        │       ├── menu/        menu_catalog.xml
        │       └── xml/         network_security_config.xml
        └── test/java/com/gestion/eventos/
            └── EventoUnitTest.kt
```

## Endpoints esperados del backend

| Método | Ruta (relativa a `http://10.0.2.2:8080/api/`) | Body | Respuesta |
|---|---|---|---|
| POST | `auth/login` | `{ "email", "password" }` | `{ "token", "mensaje", "usuario": {id,nombre,email} }` |
| POST | `auth/register` | `{ "nombre", "email", "password" }` | `{ "mensaje", ... }` |
| GET | `eventos` | — | `[ { "id", "nombre", "artista", "fecha", "tarifa" } ]` |

> Si tu backend usa otras rutas o nombres de campos, ajusta solo `ApiService.kt` y los `@SerializedName` de `model/`.

## Ejecutar en el emulador

1. Arranca el backend Spring Boot en el puerto **8080**.
2. Abre la carpeta `GestionEventos` en Android Studio → **Sync Project with Gradle Files**.
3. Ejecuta ▶ sobre un emulador (AVD). `10.0.2.2` apunta al `localhost` del PC.

## Pruebas unitarias

```bash
./gradlew test          # Windows: gradlew.bat test
```
Reporte: `app/build/reports/tests/testDebugUnitTest/index.html`

## Generar el APK

- Android Studio: **Build → Build App Bundle(s) / APK(s) → Build APK(s)**
- Consola: `./gradlew assembleDebug`
- Salida: `app/build/outputs/apk/debug/app-debug.apk`

Para APK firmado: **Build → Generate Signed App Bundle / APK → APK**.

## Control de versiones (Git)

```bash
git init
git add .
git commit -m "feat: inicialización de app móvil y vistas"
git branch -M main
git remote add origin https://github.com/<TU_USUARIO>/gestion-eventos-android.git
git push -u origin main
```
