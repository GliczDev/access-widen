@file:ApiStatus.Internal

package me.glicz.accesswiden.util

import net.fabricmc.accesswidener.AccessWidener
import net.fabricmc.accesswidener.AccessWidenerClassVisitor
import org.jetbrains.annotations.ApiStatus
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.zeroturnaround.zip.transform.ByteArrayZipEntryTransformer
import java.util.zip.ZipEntry

class AccessWidenerZipEntryTransformer(
    private val accessWidener: AccessWidener
) : ByteArrayZipEntryTransformer() {
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