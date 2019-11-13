package com.intentfilter.people.gateways

import com.intentfilter.people.models.Locations
import retrofit2.http.GET

interface LocationServiceGateway {

    @GET("en/locations/cities.json")
    suspend fun getLocations(): Locations
}
