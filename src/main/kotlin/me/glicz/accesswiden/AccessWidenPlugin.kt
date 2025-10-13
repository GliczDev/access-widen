package me.glicz.accesswiden

import org.gradle.api.Plugin
import org.gradle.api.Project

class AccessWidenPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("accessWiden", AccessWidenExtension::class.java)
    }
}