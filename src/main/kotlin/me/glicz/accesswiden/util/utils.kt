package me.glicz.accesswiden.util

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import java.io.File
import java.nio.charset.Charset

internal fun File.forceDelete() {
    setWritable(true)
    delete()
}

internal fun File.readTextIfExists(charset: Charset = Charsets.UTF_8) =
    if (exists()) readText(charset) else null

@Suppress("UNCHECKED_CAST")
internal fun <T> ExtraPropertiesExtension.getOrSet(name: String, defaultValue: () -> T): T {
    return if (has(name)) {
        get(name) as T
    } else {
        defaultValue().also { set(name, it) }
    }
}

internal fun Project.detachedConfiguration(dependencyNotation: Any) =
    configurations.detachedConfiguration(dependencies.create(dependencyNotation))
