package com.intentfilter.people.models

import com.fasterxml.jackson.annotation.JsonProperty

data class FilePath(@JsonProperty("fileName") var fileName: String) {
}