package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.parceler.ParcelConstructor

class Locations(@JsonProperty("cities") val cities: Array<City>)

@Parcel(Serialization.BEAN)
class City @ParcelConstructor constructor(
    @JsonProperty("lat") val latitude: String,
    @JsonProperty("lon") val longitude: String,
    @JsonProperty("city") val city: String
)