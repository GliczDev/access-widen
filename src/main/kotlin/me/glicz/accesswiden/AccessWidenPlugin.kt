package me.glicz.accesswiden

import me.glicz.accesswiden.task.CleanCache
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class AccessWidenPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(
            "accessWiden",
            AccessWidenExtension::class,
            project.objects,
            project
        )

        project.tasks.register<CleanCache>("cleanAccessWidenCache") {
            group = "access-widen"
        }
    }
}