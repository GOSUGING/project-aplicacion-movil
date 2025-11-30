package com.example.levelup.data.dto

data class CouponDTO(
    val id: Long,
    val code: String,
    val discount: Int,
    val active: Boolean
)