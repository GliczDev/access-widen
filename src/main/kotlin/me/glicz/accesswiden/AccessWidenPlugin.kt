package me.glicz.accesswiden

import me.glicz.accesswiden.util.AccessWidenerZipEntryTransformer
import net.fabricmc.accesswidener.AccessWidener
import net.fabricmc.accesswidener.AccessWidenerReader
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.zeroturnaround.zip.ZipUtil
import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry

class AccessWidenPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("accessWiden", AccessWidenExtension::class.java)

        val accessWiden by project.configurations.registering

        val transformedFiles = project.provider {
            val accessWidener = AccessWidener()
            val reader = AccessWidenerReader(accessWidener)

            ext.accessWideners.forEach { awFile ->
                awFile.bufferedReader().use(reader::read)
            }

            val transformer = AccessWidenerZipEntryTransformer(accessWidener)

            val entries = accessWidener.targets
                .map { target ->
                    val entryPath = target.replace(".", "/") + ".class"

                    ZipEntryTransformerEntry(entryPath, transformer)
                }
                .toTypedArray()

            accessWiden.get().files.map { input ->
                project.layout.buildDirectory.file("tmp/accessWiden/${input.name}").get().asFile.also { output ->
                    output.delete()
                    output.parentFile.mkdirs()

                    ZipUtil.transformEntries(input, entries, output)
                }
            }
        }

        project.afterEvaluate {
            ext.outputConfigurations.get().forEach { configuration ->
                project.dependencies.add(configuration.name,files(transformedFiles))
            }
        }
    }
}