package io.github.gliczdev.accesswiden.task

import net.fabricmc.accesswidener.AccessWidener
import net.fabricmc.accesswidener.AccessWidenerClassVisitor
import net.fabricmc.accesswidener.AccessWidenerReader
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.CompileClasspath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.zeroturnaround.zip.ZipUtil
import org.zeroturnaround.zip.transform.ByteArrayZipEntryTransformer
import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry
import java.util.zip.ZipEntry


abstract class ApplyAccessWideners : DefaultTask() {
    @get:InputFiles
    abstract val accessWideners: ConfigurableFileCollection

    @get:CompileClasspath
    abstract val input: ConfigurableFileCollection

    @TaskAction
    fun run() {
        val accessWidener = AccessWidener()
        val reader = AccessWidenerReader(accessWidener)

        accessWideners.files.forEach { reader.read(it.bufferedReader()) }

        val transformer = object : ByteArrayZipEntryTransformer() {
            override fun transform(zipEntry: ZipEntry, input: ByteArray): ByteArray {
                val classReader = ClassReader(input)
                val classWriter = ClassWriter(0)

                val visitor = AccessWidenerClassVisitor.createClassVisitor(
                    Opcodes.ASM9, classWriter, accessWidener
                )
                classReader.accept(visitor, 0)

                return classWriter.toByteArray()
            }
        }

        val entries = accessWidener.targets
            .map { target ->
                ZipEntryTransformerEntry(
                    target.replace(".", "/") + ".class",
                    transformer
                )
            }
            .toTypedArray()

        input.forEach { ZipUtil.transformEntries(it, entries) }
    }
}