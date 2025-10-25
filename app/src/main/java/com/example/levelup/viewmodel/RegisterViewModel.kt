package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.UserEntity
import com.example.levelup.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

// La data class para el estado de la UI se mantiene igual.
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

// --- VIEWMODEL MODIFICADO PARA HILT ---

// 1. Se anota la clase con @HiltViewModel
@HiltViewModel
// 2. El constructor ahora pide las dependencias y se anota con @Inject
class RegisterViewModel @Inject constructor(
    private val repo: UserRepository // Hilt proveerá esta instancia automáticamente
) : ViewModel() { // 3. Ya no necesita heredar de AndroidViewModel

    // 4. Se eliminan las líneas que creaban las dependencias manualmente
    // private val dao = DbModule.db(app).userDao()
    // private val repo = UserRepository(dao)

    private val _ui = MutableStateFlow(RegisterUiState())
    val ui = _ui.asStateFlow()

    // Maneja los cambios en los campos de texto
    fun onChange(field: String, value: String) {
        _ui.update {
            when (field) {
                "nombre" -> it.copy(nombre = value, error = null, success = null)
                "email" -> it.copy(email = value, error = null, success = null)
                "fechaNacimiento" -> it.copy(fechaNacimiento = value, error = null, success = null)
                "password" -> it.copy(password = value, error = null, success = null)
                "password2" -> it.copy(password2 = value, error = null, success = null)
                else -> it
            }
        }
    }

    // Se activa cuando un campo pierde el foco, para validación en tiempo real
    fun onBlur(field: String) {
        _ui.update { it.copy(touched = it.touched + field) }
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
            setError("Debes ser mayor de 18 años para registrarte"); return false // Cambiado a 18
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

        _ui.update { it.copy(error = null) } // Si todo es válido, limpia cualquier error
        return true
    }

    // Función que se llama al presionar el botón de registrar
    fun submit() {
        _ui.update { it.copy(touched = setOf("nombre", "email", "fechaNacimiento", "password", "password2")) }
        if (!validateForm(onlyTouched = false)) return

        val s = _ui.value
        viewModelScope.launch {
            val user = UserEntity(
                nombre = s.nombre.trim(),
                email = s.email.trim().lowercase(),
                fechaNacimiento = s.fechaNacimiento.trim(),
                passwordHash = s.password // En producción, esto debería ser un hash
            )

            val result = repo.register(user)
            result.fold(
                onSuccess = {
                    _ui.update { RegisterUiState(success = "¡Registro exitoso! Ya puedes iniciar sesión") }
                },
                onFailure = { e ->
                    val msg = if (e.message?.contains("UNIQUE", true) == true) {
                        "El correo electrónico ya está registrado"
                    } else {
                        "Ocurrió un error inesperado durante el registro"
                    }
                    setError(msg)
                }
            )
        }
    }

    private fun setError(msg: String) {
        _ui.update { it.copy(error = msg, success = null) }
    }

    // --- Funciones de Validación ---
    private fun validateEmail(email: String): Boolean =
        Regex("\\S+@\\S+\\.\\S+").matches(email)

    private fun validatePassword(pw: String): Boolean =
        Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*\\-])(?=.{8,}).*").matches(pw)

    private fun validateAge(dateStr: String): Boolean = try {
        val birth = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
        val today = LocalDate.now()
        var age = today.year - birth.year
        if (today.dayOfYear < birth.dayOfYear) age--
        age >= 18 // Cambiado a 18 años
    } catch (_: DateTimeParseException) {
        false
    }
}
