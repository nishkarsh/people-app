package com.intentfilter.people.services

import com.intentfilter.people.gateways.AttributeServiceGateway
import com.intentfilter.people.models.SingleChoiceAttributes
import javax.inject.Inject

class AttributeService @Inject constructor(private val gateway: AttributeServiceGateway) {

    suspend fun getAttributes(): SingleChoiceAttributes = gateway.getAttributes()
}