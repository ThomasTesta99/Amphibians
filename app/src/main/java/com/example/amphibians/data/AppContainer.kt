package com.example.amphibians.data

import com.example.amphibians.network.AmphibianApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val amphibianPhotoRepository : AmphibianPhotoRepository
}

class DefaultAppContainer: AppContainer{
    private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    val retrofitService : AmphibianApiService by lazy{
        retrofit.create(AmphibianApiService::class.java)
    }

    override val amphibianPhotoRepository: AmphibianPhotoRepository by lazy {
        NetworkAmphibianPhotoRepository(retrofitService)
    }
}
