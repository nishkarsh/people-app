package com.intentfilter.people.services

import com.intentfilter.people.gateways.LocationServiceGateway
import com.intentfilter.people.models.Locations
import javax.inject.Inject

open class LocationService @Inject constructor(private val gateway: LocationServiceGateway) {

    open suspend fun getLocations(): Locations {
        return gateway.getLocations()
    }
}
