package com.intentfilter.people.gateways

import com.intentfilter.people.models.SingleChoiceAttributes
import retrofit2.http.GET

interface AttributeServiceGateway {

    @GET("/en/single_choice_attributes.json")
    suspend fun getAttributes(): SingleChoiceAttributes
}