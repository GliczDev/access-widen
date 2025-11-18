package me.glicz.accesswiden.util

import java.io.File
import java.io.InputStream
import java.security.MessageDigest

object MessageDigests {
    fun sha256() = MessageDigest.getInstance("SHA-256")!!
}

fun MessageDigest.update(inputStream: InputStream) {
    val buffer = ByteArray(8 * 1024 * 1024) // 8 MB

    var read: Int
    while (inputStream.read(buffer).also { read = it } != -1) {
        update(buffer, 0, read)
    }
}

fun MessageDigest.digest(file: File) = digest(file.readBytes())!!
