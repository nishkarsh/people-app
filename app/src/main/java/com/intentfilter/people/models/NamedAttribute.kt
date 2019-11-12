package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.parceler.ParcelConstructor
import java.util.*

@Parcel(Serialization.BEAN)
class NamedAttribute @ParcelConstructor constructor(
    @JsonProperty("id") var id: UUID,
    @JsonProperty("name") var name: String
)