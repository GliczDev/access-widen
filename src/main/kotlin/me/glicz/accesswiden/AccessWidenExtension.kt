package me.glicz.accesswiden

import me.glicz.accesswiden.util.accessWiden0
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

abstract class AccessWidenExtension(
    objects: ObjectFactory,
    private val project: Project,
) {
    val accessWideners = objects.fileCollection()

    @JvmName("dependency")
    operator fun invoke(dependency: Any) = project.accessWiden0(dependency)
}