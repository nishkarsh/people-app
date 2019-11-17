package com.intentfilter.people.gateways

import com.intentfilter.people.models.Profile
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ProfileServiceGateway {

    @GET("/profile/{id}")
    suspend fun get(@Path("id") id: String): Profile

    @POST("/profile")
    suspend fun create(@Body profile: Profile): Profile

    @PUT("/profile/{id}")
    fun update(@Path("id") id: String, @Body profile: Profile): Call<Response<Unit>>

    @Multipart
    @POST("/profile/picture")
    suspend fun uploadPicture(@Part picture: MultipartBody.Part): String
}