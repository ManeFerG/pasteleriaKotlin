package com.example.pasteleria.network

import com.example.pasteleria.model.Usuario
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// Interfaz de la API
interface ApiService {
    @POST("/usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<ResponseBody>

    @POST("/usuarios/login")
    suspend fun login(@Body loginData: Map<String, String>): Response<Usuario>
}

// Singleton para Retrofit
object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.23:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}