package com.example.levelup.data

import com.example.levelup.data.network.AuthApi
import com.example.levelup.data.network.CartApi
import com.example.levelup.data.network.ProductApi
import com.example.levelup.data.network.CouponsApi
import com.example.levelup.data.network.PaymentApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val AUTH_BASE_URL = "http://56.228.34.53:8081/api/auth/"
    private const val CART_BASE_URL = "http://56.228.34.53:8082/api/cart/"
    private const val PRODUCT_BASE_URL = "http://56.228.34.53:8085/api/v1/products/"
    private const val COUPON_BASE_URL = "http://56.228.34.53:8084/api/coupons/"
    private const val PAYMENT_BASE_URL = "http://56.228.34.53:8083/api/payments/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // === AUTH ===
    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val authApi: AuthApi by lazy {
        authRetrofit.create(AuthApi::class.java)
    }

    // === CART ===
    private val cartRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(CART_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val cartApi: CartApi by lazy {
        cartRetrofit.create(CartApi::class.java)
    }

    // === PRODUCTS ===
    private val productRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PRODUCT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val productApi: ProductApi by lazy {
        productRetrofit.create(ProductApi::class.java)
    }

    // === COUPONS ===
    private val couponRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(COUPON_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val couponsApi: CouponsApi by lazy {
        couponRetrofit.create(CouponsApi::class.java)
    }

    // === PAYMENT ===
    private val paymentRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PAYMENT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val paymentApi: PaymentApi by lazy {
        paymentRetrofit.create(PaymentApi::class.java)
    }
}
