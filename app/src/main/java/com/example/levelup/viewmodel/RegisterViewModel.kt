package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.RegisterRequest
import com.example.levelup.data.dto.RegisterResponse
import com.example.levelup.data.network.AuthApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

// ===============================================================
// UI STATE
// ===============================================================
data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val fechaNacimiento: String = "",
    val phone: String = "",
    val address: String = "",
    val password: String = "",
    val password2: String = "",
    val error: String? = null,
    val success: String? = null,
    val touched: Set<String> = emptySet()
)

// ===============================================================
// VIEWMODEL
// ===============================================================
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: AuthApi
) : ViewModel() {

    private val _ui = MutableStateFlow(RegisterUiState())
    val ui = _ui.asStateFlow()

    // Cambios de campos
    fun onChange(field: String, value: String) {
        _ui.update {
            when (field) {
                "nombre" -> it.copy(nombre = value, error = null)
                "email" -> it.copy(email = value, error = null)
                "fechaNacimiento" -> it.copy(fechaNacimiento = value, error = null)
                "phone" -> it.copy(phone = value, error = null)
                "address" -> it.copy(address = value, error = null)
                "password" -> it.copy(password = value, error = null)
                "password2" -> it.copy(password2 = value, error = null)
                else -> it
            }
        }
    }

    // Marcamos campo tocado
    fun onBlur(field: String) {
        _ui.update { it.copy(touched = it.touched + field) }
        validateIfTouched()
    }

    private fun validateIfTouched(): Boolean = validateForm(onlyTouched = true)

    // ===============================================================
    // VALIDACIONES
    // ===============================================================
    private fun validateForm(onlyTouched: Boolean = false): Boolean {
        val s = _ui.value
        fun touched(f: String) = !onlyTouched || s.touched.contains(f)

        if (touched("nombre") && s.nombre.isBlank())
            return error("Por favor ingresa tu nombre completo")

        if (touched("email") && s.email.isBlank())
            return error("Por favor ingresa tu correo")

        if (touched("email") && !validateEmail(s.email))
            return error("Ingresa un correo electrónico válido")

        if (touched("fechaNacimiento") && s.fechaNacimiento.isBlank())
            return error("Ingresa tu fecha de nacimiento")

        if (touched("fechaNacimiento") && !validateAge(s.fechaNacimiento))
            return error("Debes ser mayor de 18 años para registrarte")

        if (touched("phone") && s.phone.isBlank())
            return error("Por favor ingresa tu número telefónico")

        if (touched("address") && s.address.isBlank())
            return error("Por favor ingresa tu dirección")

        if (touched("password") && s.password.isBlank())
            return error("Ingresa una contraseña válida")

        if (touched("password") && !validatePassword(s.password))
            return error("La contraseña debe tener 8 caracteres, un número y un símbolo")

        if (touched("password2") && s.password2 != s.password)
            return error("Las contraseñas no coinciden")

        _ui.update { it.copy(error = null) }
        return true
    }

    private fun error(msg: String): Boolean {
        _ui.update { it.copy(error = msg, success = null) }
        return false
    }

    private fun validateEmail(email: String): Boolean =
        Regex("\\S+@\\S+\\.\\S+").matches(email)

    private fun validatePassword(pw: String): Boolean =
        Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*\\-])(?=.{8,}).*").matches(pw)

    private fun validateAge(dateStr: String): Boolean = try {
        val birth = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
        val today = LocalDate.now()
        var age = today.year - birth.year
        if (today.dayOfYear < birth.dayOfYear) age--
        age >= 18
    } catch (_: DateTimeParseException) {
        false
    }

    // ===============================================================
    // SUBMIT → ENVÍA REQUEST REAL AL AUTH API
    // ===============================================================
    fun submit() {
        // marcamos todos los campos
        _ui.update {
            it.copy(
                touched = setOf("nombre", "email", "fechaNacimiento", "phone", "address", "password", "password2")
            )
        }

        if (!validateForm(false)) return

        val s = _ui.value

        val req = RegisterRequest(
            name = s.nombre.trim(),
            email = s.email.trim(),
            password = s.password.trim(),
            phone = s.phone.trim(),
            address = s.address.trim(),
            fechaNacimiento = s.fechaNacimiento.trim(),
            role = "USER"
        )

        viewModelScope.launch {
            try {
                val res: RegisterResponse = api.register(req)

                _ui.update {
                    RegisterUiState(
                        success = res.message,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _ui.update {
                    it.copy(
                        error = e.message ?: "Error al registrar",
                        success = null
                    )
                }
            }
        }
    }
}
