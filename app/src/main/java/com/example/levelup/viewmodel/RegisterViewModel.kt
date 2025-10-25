package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.DbModule
import com.example.levelup.data.UserEntity
import com.example.levelup.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Data class para representar el estado de la UI del formulario de registro
data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val fechaNacimiento: String = "",
    val password: String = "",
    val password2: String = "",
    val error: String? = null,
    val success: String? = null,
    val touched: Set<String> = emptySet()
)

class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    // Inicialización del acceso a la base de datos a través del repositorio
    private val dao = DbModule.db(app).userDao()
    private val repo = UserRepository(dao)

    // StateFlow para exponer el estado de la UI de forma reactiva
    private val _ui = MutableStateFlow(RegisterUiState())
    val ui = _ui.asStateFlow()

    // Maneja los cambios en los campos de texto
    fun onChange(field: String, value: String) {
        _ui.value = when (field) {
            "nombre" -> _ui.value.copy(nombre = value, error = null, success = null)
            "email" -> _ui.value.copy(email = value, error = null, success = null)
            "fechaNacimiento" -> _ui.value.copy(fechaNacimiento = value, error = null, success = null)
            "password" -> _ui.value.copy(password = value, error = null, success = null)
            "password2" -> _ui.value.copy(password2 = value, error = null, success = null)
            else -> _ui.value
        }
    }

    // Se activa cuando un campo pierde el foco, para validación en tiempo real
    fun onBlur(field: String) {
        _ui.value = _ui.value.copy(touched = _ui.value.touched + field)
        validateIfTouched()
    }

    private fun validateIfTouched(): Boolean = validateForm(onlyTouched = true)

    // Lógica principal de validación del formulario
    private fun validateForm(onlyTouched: Boolean = false): Boolean {
        val s = _ui.value
        fun touch(field: String) = !onlyTouched || s.touched.contains(field)

        if (touch("nombre") && s.nombre.isBlank()) {
            setError("Por favor ingrese su nombre completo"); return false
        }
        if (touch("email") && s.email.isBlank()) {
            setError("Por favor ingrese su correo electrónico"); return false
        }
        if (touch("email") && s.email.isNotBlank() && !validateEmail(s.email)) {
            setError("Por favor ingrese un correo electrónico válido"); return false
        }
        if (touch("fechaNacimiento") && s.fechaNacimiento.isBlank()) {
            setError("Por favor ingrese su fecha de nacimiento"); return false
        }
        if (touch("fechaNacimiento") && s.fechaNacimiento.isNotBlank() && !validateAge(s.fechaNacimiento)) {
            setError("Debes tener al menos 13 años para registrarte"); return false
        }
        if (touch("password") && s.password.isBlank()) {
            setError("Por favor ingrese una contraseña"); return false
        }
        if (touch("password") && s.password.isNotBlank() && !validatePassword(s.password)) {
            setError("La contraseña debe tener al menos 8 caracteres, incluir un número y un símbolo"); return false
        }
        if (touch("password2") && s.password2.isBlank()) {
            setError("Por favor confirme su contraseña"); return false
        }
        if (touch("password2") && s.password2.isNotBlank() && s.password != s.password2) {
            setError("Las contraseñas no coinciden"); return false
        }

        _ui.value = _ui.value.copy(error = null) // Si todo es válido, limpia cualquier error
        return true
    }

    // Función que se llama al presionar el botón de registrar
    fun submit() {
        // Marca todos los campos como "tocados" para forzar la validación de todos
        _ui.value = _ui.value.copy(touched = setOf("nombre", "email", "fechaNacimiento", "password", "password2"))
        if (!validateForm(onlyTouched = false)) return

        val s = _ui.value
        viewModelScope.launch {
            // NOTA DE SEGURIDAD: En una app real, nunca guardes la contraseña en texto plano.
            // Debes usar una biblioteca de hashing como BCrypt.
            val user = UserEntity(
                nombre = s.nombre.trim(),
                email = s.email.trim().lowercase(),
                fechaNacimiento = s.fechaNacimiento.trim(),
                passwordHash = s.password // En producción, esto debería ser hash(s.password)
            )

            // Llama al repositorio para registrar al usuario
            val result = repo.register(user)
            result.fold(
                onSuccess = {
                    // Limpia el formulario y muestra un mensaje de éxito
                    _ui.value = RegisterUiState(success = "¡Registro exitoso! Ya puedes iniciar sesión")
                },
                onFailure = { e ->
                    // Maneja errores específicos, como un email duplicado
                    val msg = if (e.message?.contains("UNIQUE", true) == true) {
                        "El correo ya está registrado"
                    } else "Ocurrió un error al registrar"
                    setError(msg)
                }
            )
        }
    }

    private fun setError(msg: String) {
        _ui.value = _ui.value.copy(error = msg, success = null)
    }

    // --- Funciones de Validación ---
    private fun validateEmail(email: String): Boolean =
        Regex("\\S+@\\S+\\.\\S+").matches(email)

    private fun validatePassword(pw: String): Boolean =
        // Regex: Al menos 8 caracteres, 1 número, 1 símbolo
        Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*\\-])(?=.{8,}).*").matches(pw)

    private fun validateAge(dateStr: String): Boolean = try {
        val birth = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
        val today = LocalDate.now()
        var age = today.year - birth.year
        // Ajusta la edad si el cumpleaños de este año aún no ha pasado
        if (today < birth.plusYears(age.toLong())) age--
        age >= 13
    } catch (_: DateTimeParseException) {
        // Si la fecha no está en formato ISO (YYYY-MM-DD), la validación falla
        false
    }
}
