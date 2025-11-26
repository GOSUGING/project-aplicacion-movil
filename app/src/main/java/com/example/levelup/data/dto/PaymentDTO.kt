package com.example.levelup.data.dto

// Asumo que el campo 'total' es un entero grande (Long) ya que no tiene decimales en la imagen,
// y que 'cantidad' es un entero (Int).

data class PaymentDTO(
    val id: Long, // Columna 'id'
    val cantidad: Int, // Columna 'cantidad' (total de unidades)
    val total: Long, // Columna 'total' (usamos Long para montos grandes sin decimales)
    val estado: String?, // Columna 'estado' (hacemos opcional por seguridad, aunque el JSON no lo muestra nulo)

    // CAMPOS QUE SON NULL O SE MANDAN VACÍOS EN LA BASE DE DATOS/JSON:
    val fecha: String?, // Columna 'fecha' (Es NULL o vacío en muchas filas)
    val nombreUsuario: String?, // Columna 'nombreUsuario' (Es NULL o vacío en muchas filas)
    val direccionEnvio: String?, // Columna 'direccionEnvio' (Es NULL o vacío en muchas filas)

    // El DTO original no los incluye, pero son parte del JSON que mostraste:
    val userId: Long, // (Asumiendo que este es el campo que usas para el usuario)
    val rawPayload: String // Columna 'raw_payload' (JSON de los productos)
)