@file:ApiStatus.Internal

package me.glicz.accesswiden.util

import org.jetbrains.annotations.ApiStatus

class Hash(private val bytes: ByteArray) {
    fun toHexString() = bytes.toHexString()

    fun contentEquals(other: ByteArray?) = bytes.contentEquals(other)
}
