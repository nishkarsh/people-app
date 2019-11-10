package com.intentfilter.people.providers

import com.intentfilter.people.BuildConfig
import com.intentfilter.people.gateways.AttributeServiceGateway
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class GatewayProvider {

    @Provides
    fun provideAttributeServiceGateway(): AttributeServiceGateway {
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.ATTRIBUTE_SERVICE_BASE_URL).build()
        return retrofit.create(AttributeServiceGateway::class.java)
    }
}