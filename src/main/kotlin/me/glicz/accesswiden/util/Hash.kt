package me.glicz.accesswiden.util

internal class Hash(private val bytes: ByteArray) {
    constructor(hash: String) : this(
        hash.chunked(2) { it.toString().toInt(16).toByte() }.toByteArray()
    )

    override fun hashCode() = bytes.contentHashCode()

    override fun equals(other: Any?) =
        this === other || other is Hash && bytes.contentEquals(other.bytes)

    override fun toString() =
        bytes.joinToString("") { "%02x".format(it) }
}
