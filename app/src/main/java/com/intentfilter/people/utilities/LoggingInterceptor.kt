package com.intentfilter.people.utilities

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

open class LoggingInterceptor : Interceptor {
    private val logger = Logger.loggerFor(LoggingInterceptor::class)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val t1 = System.nanoTime()
        logger.d("Sending request to ${request.url()} with ${request.headers()}")

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        logger.d(
            String.format(
                "Received response for ${response.request().url()} in %.1fms %n%s with status code ${response.code()}",
                (t2 - t1) / 1e6, response.headers()
            )
        )
        return response
    }
}