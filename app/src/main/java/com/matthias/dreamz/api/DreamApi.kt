package com.matthias.dreamz.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DreamApi {
    @GET("dream")
    suspend fun getDreams(): Result<List<DreamDayDto>>

    @POST("dream")
    suspend fun saveDream(@Body dream: DreamDayDto): DreamDayDto
}