package io.github.gliczdev.accesswiden

import io.github.gliczdev.accesswiden.task.ApplyAccessWideners
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

class AccessWidenPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("accessWideners", AccessWidenersExtension::class.java)

        val accessWiden by project.configurations.registering

        val applyAccessWideners by project.tasks.registering(ApplyAccessWideners::class) {
            accessWideners.from(ext.files)

            input.from(accessWiden)
        }
    }
}