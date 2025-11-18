package me.glicz.accesswiden.util

import java.io.File
import java.nio.charset.Charset

fun File.forceDelete() {
    setWritable(true)
    delete()
}

fun File.readTextIfExists(charset: Charset = Charsets.UTF_8) =
    if (exists()) readText(charset) else null

fun ByteArray.toHexString() =
    joinToString("") { "%02x".format(it) }

fun String.toHexByteArray() =
    chunked(2) { it.toString().toInt(16).toByte() }.toByteArray()
