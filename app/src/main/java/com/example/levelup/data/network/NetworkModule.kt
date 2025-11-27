package com.example.levelup.di

import com.example.levelup.data.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val HOST = "http://56.228.34.53"

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    // ======================================================
    // ⭐ AUTH + USERS (MISMO SERVICIO 8081)
    // ======================================================
    @Provides
    @Singleton
    fun provideAuthRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("$HOST:8081/")        // <-- AUTH + USERS AQUÍ
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    // ======================================================
    // ⭐ PRODUCTOS (8085)
    // ======================================================
    @Provides
    @Singleton
    fun provideProductApi(client: OkHttpClient): ProductApi =
        Retrofit.Builder()
            .baseUrl("$HOST:8085/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ProductApi::class.java)

    // ======================================================
    // ⭐ CARRITO (8082)
    // ======================================================
    @Provides
    @Singleton
    fun provideCartApi(client: OkHttpClient): CartApi =
        Retrofit.Builder()
            .baseUrl("$HOST:8082/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CartApi::class.java)

    // ======================================================
    // ⭐ CUPONES (8084)
    // ======================================================
    @Provides
    @Singleton
    fun provideCouponsApi(client: OkHttpClient): CouponsApi =
        Retrofit.Builder()
            .baseUrl("$HOST:8084/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CouponsApi::class.java)

    // ======================================================
    // ⭐ PAGOS (8083)
    // ======================================================
    @Provides
    @Singleton
    fun providePaymentApi(client: OkHttpClient): PaymentApi =
        Retrofit.Builder()
            .baseUrl("$HOST:8083/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PaymentApi::class.java)
}
