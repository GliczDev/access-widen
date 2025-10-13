package me.glicz.accesswiden

import org.gradle.api.model.ObjectFactory

abstract class AccessWidenExtension(
    objects: ObjectFactory
) {
    val accessWideners = objects.fileCollection()
}