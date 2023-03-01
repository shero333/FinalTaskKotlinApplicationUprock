package com.example.finaltaskkotlinapplication.repository.api

import com.example.finaltaskkotlinapplication.models.UnsplashImageModel
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {

    companion object {
        private const val API_KEY = "8DoBBzOSak1o6LJIxArR2RfVKX5kzhEI6zBFL4KIWhY"
    }
    @Headers("Authorization: Client-ID $API_KEY")
    @GET("/photos")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashImageModel
}