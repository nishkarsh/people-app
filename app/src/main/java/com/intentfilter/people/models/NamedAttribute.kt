package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class NamedAttribute(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("name") val name: String
)