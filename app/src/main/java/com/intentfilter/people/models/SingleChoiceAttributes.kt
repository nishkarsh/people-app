package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty

class SingleChoiceAttributes(
    @JsonProperty("gender") val gender: Array<NamedAttribute>,
    @JsonProperty("ethnicity") val ethnicity: Array<NamedAttribute>,
    @JsonProperty("religion") val religion: Array<NamedAttribute>,
    @JsonProperty("figure") val figure: Array<NamedAttribute>,
    @JsonProperty("marital_status") val maritalStatus: Array<NamedAttribute>
)