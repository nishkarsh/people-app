package com.intentfilter.people.utilities

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException

object TempImageFileBuilder {
    private val logger = Logger.loggerFor(TempImageFileBuilder::class)

    @Throws(IOException::class)
    fun createFile(context: Context, uri: Uri): File {
        val tempFile = File.createTempFile("intent-", ".jpg")

        tempFile.outputStream().use { outputStream ->
            context.contentResolver.openInputStream(uri).use { inputStream ->
                val bytesCopied = inputStream?.copyTo(outputStream)
                logger.d("Copied $bytesCopied to the temporary picture file created.")
            }
        }

        return tempFile
    }
}