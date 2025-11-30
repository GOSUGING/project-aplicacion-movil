package com.example.levelup.data.dto

import com.google.gson.annotations.SerializedName

data class PaymentDTO(
    @SerializedName("id")
    val id: Long,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("total")
    val total: Long,

    @SerializedName("estado")
    val estado: String?,

    @SerializedName("fecha")
    val fecha: String?,

    @SerializedName("nombreUsuario")
    val nombreUsuario: String?,

    @SerializedName("direccionEnvio")
    val direccionEnvio: String?,

    @SerializedName("userId")
    val userId: Long,

    @SerializedName("raw_payload")
    val rawPayload: String
)
