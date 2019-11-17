package com.intentfilter.people.providers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.intentfilter.people.BuildConfig
import com.intentfilter.people.gateways.AttributeServiceGateway
import com.intentfilter.people.gateways.LocationServiceGateway
import com.intentfilter.people.gateways.ProfileServiceGateway
import com.intentfilter.people.utilities.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
class GatewayModule {

    @Provides
    @Singleton
    fun provideAttributeServiceGateway(client: OkHttpClient, mapper: ObjectMapper): AttributeServiceGateway {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .baseUrl(BuildConfig.ATTRIBUTE_SERVICE_BASE_URL)
            .client(client)
            .build()

        return retrofit.create(AttributeServiceGateway::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationServiceGateway(client: OkHttpClient, mapper: ObjectMapper): LocationServiceGateway {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .baseUrl(BuildConfig.LOCATION_SERVICE_BASE_URL)
            .client(client)
            .build()

        return retrofit.create(LocationServiceGateway::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileServiceGateway(client: OkHttpClient, mapper: ObjectMapper): ProfileServiceGateway {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .baseUrl(BuildConfig.PROFILE_SERVICE_BASE_URL)
            .client(client)
            .build()

        return retrofit.create(ProfileServiceGateway::class.java)
    }

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper {
        return ObjectMapper().also {
            it.configure(MapperFeature.AUTO_DETECT_FIELDS, true)
            it.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(loggingInterceptor: LoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addNetworkInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor()
    }
}