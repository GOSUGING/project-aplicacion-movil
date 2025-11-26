package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.network.ProductApi
import com.example.levelup.data.network.PaymentApi
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.dto.PaymentDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val productApi: ProductApi,
    private val paymentApi: PaymentApi
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductDTO>>(emptyList())
    val products = _products.asStateFlow()

    private val _payments = MutableStateFlow<List<PaymentDTO>>(emptyList())
    val payments = _payments.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _products.value = productApi.getProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateStock(id: Long, amount: Int) {
        viewModelScope.launch {
            try {
                productApi.updateStock(id, amount)
                loadProducts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadPayments() {
        viewModelScope.launch {
            try {
                _payments.value = paymentApi.getPayments()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
