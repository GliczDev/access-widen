package io.github.gliczdev.accesswiden

import org.gradle.api.model.ObjectFactory

abstract class AccessWidenersExtension(objects: ObjectFactory) {
    val files = objects.fileCollection()
}