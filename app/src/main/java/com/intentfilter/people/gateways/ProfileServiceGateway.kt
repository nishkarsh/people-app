package com.intentfilter.people.gateways

import com.intentfilter.people.models.Profile
import retrofit2.http.*

interface ProfileServiceGateway {

    @GET("/profile/{id}")
    suspend fun get(@Path("id") id: String): Profile

    @POST("/profile")
    suspend fun create(@Body profile: Profile): Profile

    @PUT("/profile/{id}")
    suspend fun update(@Path("id") id: String, @Body profile: Profile)
}