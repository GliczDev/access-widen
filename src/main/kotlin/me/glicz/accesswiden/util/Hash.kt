package me.glicz.accesswiden.util

class Hash(private val bytes: ByteArray) {
    fun toHexString() = bytes.toHexString()

    fun contentEquals(other: ByteArray?) = bytes.contentEquals(other)
}
