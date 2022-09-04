package ru.mts.data.utils

import java.io.FileNotFoundException
import java.net.URL

object ResourcesReader {

    fun readText(path: String): String {
        val inputStream = ResourcesReader::class.java.classLoader?.getResourceAsStream(path)
            ?: throw FileNotFoundException("File [$path] not found.")
        return inputStream.bufferedReader().readText()
    }
}