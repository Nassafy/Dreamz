package com.matthias.dreamz.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    fun auth(@Body authDto: AuthDto): Call<ResponseBody>
}