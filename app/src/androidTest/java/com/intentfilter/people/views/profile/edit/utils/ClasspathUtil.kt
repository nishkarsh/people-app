package com.intentfilter.people.views.profile.edit.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

object ClasspathUtil {

    fun fromClassPath(relativePath: String): String {
        val resourceStream = Thread.currentThread().contextClassLoader!!.getResourceAsStream(relativePath)
        val writer = StringWriter()
        val buffer = CharArray(1024)

        try {
            val reader = BufferedReader(InputStreamReader(resourceStream, "UTF-8"))
            var numBytes = reader.read(buffer)
            while ((numBytes) != -1) {
                writer.write(buffer, 0, numBytes)
                numBytes = reader.read(buffer)
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            resourceStream.close()
        }

        return writer.toString()
    }
}