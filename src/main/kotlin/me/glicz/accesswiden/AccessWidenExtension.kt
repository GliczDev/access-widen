package me.glicz.accesswiden

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.*

abstract class AccessWidenExtension(
    project: Project,
    objects: ObjectFactory
) {
    val accessWideners = objects.fileCollection()

    val outputConfigurations = objects.setProperty<Configuration>().convention(
        project.provider {
            setOf(project.configurations.named(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME).get())
        }
    )
}