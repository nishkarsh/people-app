package com.intentfilter.people.services

import com.intentfilter.people.gateways.AttributeServiceGateway
import com.intentfilter.people.models.SingleChoiceAttributes
import javax.inject.Inject

open class AttributeService @Inject constructor(private val gateway: AttributeServiceGateway) {

    open suspend fun getAttributes(): SingleChoiceAttributes = gateway.getAttributes()
}