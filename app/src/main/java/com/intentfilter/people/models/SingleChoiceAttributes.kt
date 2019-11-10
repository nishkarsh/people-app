package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty

class SingleChoiceAttributes(
    private val gender: Array<NamedAttribute>,
    private val ethnicity: Array<NamedAttribute>,
    private val religion: Array<NamedAttribute>,
    private val figure: Array<NamedAttribute>,
    @JsonProperty("marital_status") private val maritalStatus: Array<NamedAttribute>
)