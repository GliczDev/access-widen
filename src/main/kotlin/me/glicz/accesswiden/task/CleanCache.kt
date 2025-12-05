package me.glicz.accesswiden.task

import me.glicz.accesswiden.util.CACHE_DIR
import me.glicz.accesswiden.util.forceDelete
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class CleanCache : DefaultTask() {
    @TaskAction
    fun run() {
        project.rootDir.resolve(CACHE_DIR).walkBottomUp().forEach(File::forceDelete)
    }
}