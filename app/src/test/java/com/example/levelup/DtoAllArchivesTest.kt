package com.example.levelup

import com.example.levelup.data.dto.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DtoAllArchivesTest {

    @Test
    fun createAllDtos() {
        val p = ProductDTO(1L, "Mouse", "Optico", 300, "/img", "peripherals", 10)
        assertEquals(1L, p.id)

        val user = UserDTO(2L, "Ana", "a@a.com", null, null, "USER")
        assertEquals("Ana", user.name)

        val loginReq = LoginRequest("a@a.com", "1234")
        assertEquals("a@a.com", loginReq.email)

        val loginRes = LoginResponse(2L, "Ana", "a@a.com", "USER")
        assertEquals("USER", loginRes.role)

        val regReq = RegisterRequest("Ana", "a@a.com", "2000-01-01", "555", "dir", "1234")
        assertEquals("Ana", regReq.name)

        val regRes = RegisterResponse(2L, "Ana", "a@a.com", "USER", "tkn", "OK")
        assertEquals("OK", regRes.message)

        val coupon = CouponDTO(10L, "PROMO10", 10, true)
        assertEquals(true, coupon.active)

        val item = CheckoutItem(1L, 2, 300)
        assertEquals(2, item.cantidad)

        val req = CheckoutRequest(1L, listOf(item), 600, "U", "Dir", CardPaymentDTO("4111111111111111", "U", 12, 2030, "123"))
        assertEquals(600, req.total)

        val resp = CheckoutResponseDTO("OK")
        assertEquals("OK", resp.message)

        val cartItemPayload = CartItemPayload(1L, "Mouse", 2, 300)
        assertEquals(2, cartItemPayload.qty)

        val addItemRequest = AddItemRequest(1L, 2, "Mouse", 300, "/img")
        assertEquals("Mouse", addItemRequest.name)

        val cartResponse = CartResponse(1L, emptyList(), 0, 0.0)
        assertEquals(0, cartResponse.totalItems)

        val replaceReq = ReplaceCartRequest(listOf(addItemRequest))
        assertEquals(1, replaceReq.items.size)

        val updateQty = UpdateQuantityRequest(5)
        assertEquals(5, updateQty.quantity)

        val updateUser = UpdateUserRequest(name = "Ana", email = "a@a.com", address = null, phone = null, role = "ADMIN")
        assertEquals("ADMIN", updateUser.role)

        val payment = PaymentDTO(
            id = 1L,
            cantidad = 2,
            total = 600L,
            estado = "OK",
            fecha = "2024-01-01",
            nombreUsuario = "U",
            direccionEnvio = "Dir",
            userId = 7L,
            rawPayload = "{}"
        )
        assertEquals(600L, payment.total)
    }

    @Test
    fun registerRequestFieldsAreMapped() {
        val req = RegisterRequest(
            name = "Ana",
            email = "a@a.com",
            password = "1234",
            address = "dir",
            phone = "555",
            fechaNacimiento = "2000-01-01"
        )
        assertEquals("Ana", req.name)
        assertEquals("a@a.com", req.email)
        assertEquals("1234", req.password)
        assertEquals("dir", req.address)
        assertEquals("555", req.phone)
        assertEquals("2000-01-01", req.fechaNacimiento)
        assertEquals("USER", req.role)
    }

    @Test
    fun registerResponseHoldsTokenAndRole() {
        val res = RegisterResponse(
            id = 2L,
            name = "Ana",
            email = "a@a.com",
            role = "USER",
            token = "tkn",
            message = "OK"
        )
        assertEquals(2L, res.id)
        assertEquals("USER", res.role)
        assertEquals("tkn", res.token)
        assertEquals("OK", res.message)
    }

    @Test
    fun updateUserRequestAcceptsNullsAndRole() {
        val up = UpdateUserRequest(
            name = "Ana",
            email = "a@a.com",
            address = null,
            phone = null,
            role = "ADMIN"
        )
        assertEquals("Ana", up.name)
        assertEquals("a@a.com", up.email)
        assertEquals("ADMIN", up.role)
    }

    @Test
    fun cartItemPayloadFieldsAreCorrect() {
        val payload = CartItemPayload(
            productId = 1L,
            name = "Mouse",
            qty = 2,
            price = 300
        )
        assertEquals(1L, payload.productId)
        assertEquals("Mouse", payload.name)
        assertEquals(2, payload.qty)
        assertEquals(300, payload.price)
    }
}
