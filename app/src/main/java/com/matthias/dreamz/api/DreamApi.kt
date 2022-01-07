package com.matthias.dreamz.api

import retrofit2.Response
import retrofit2.http.*

interface DreamApi {
    @GET("dream")
    suspend fun getDreams(): Result<List<DreamDayDto>>

    @POST("dream")
    suspend fun saveDream(@Body dream: DreamDayDto): DreamDayDto

    @DELETE("dream/{dreamId}")
    suspend fun deleteDream(@Path("dreamId") dreamId: String): Response<Unit>
}