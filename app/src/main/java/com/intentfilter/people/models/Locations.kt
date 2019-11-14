package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty

class Locations(@JsonProperty("cities") val cities: Array<City>)

class City(
    @JsonProperty("lat") val latitude: String,
    @JsonProperty("lon") val longitude: String,
    @JsonProperty("city") val name: String
)