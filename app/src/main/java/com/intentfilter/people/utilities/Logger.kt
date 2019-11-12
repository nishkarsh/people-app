package com.intentfilter.people.utilities

import android.util.Log
import com.intentfilter.people.BuildConfig
import kotlin.reflect.KClass

class Logger private constructor(clazz: KClass<*>) {
    private val logTag: String = clazz.java.name

    fun d(message: String) {
        if (loggingEnabled()) {
            Log.d(logTag, message)
        }
    }

    fun d(message: String, throwable: Throwable) {
        if (loggingEnabled()) {
            Log.d(logTag, message, throwable)
        }
    }

    fun i(message: String) {
        if (loggingEnabled()) {
            Log.i(logTag, message)
        }
    }

    fun e(message: String) {
        if (loggingEnabled()) {
            Log.e(logTag, message)
        }
    }

    fun e(message: String, throwable: Throwable) {
        if (loggingEnabled()) {
            Log.e(logTag, message, throwable)
        }
    }

    companion object {
        fun loggerFor(clazz: KClass<*>): Logger {
            return Logger(clazz)
        }

        private fun loggingEnabled(): Boolean {
            return BuildConfig.DEBUG
        }
    }
}