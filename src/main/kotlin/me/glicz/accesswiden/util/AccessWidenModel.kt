package me.glicz.accesswiden.util

import me.glicz.accesswiden.AccessWidenExtension
import net.fabricmc.accesswidener.AccessWidener
import net.fabricmc.accesswidener.AccessWidenerReader
import org.gradle.api.Project
import org.gradle.internal.extensions.core.extra
import org.gradle.kotlin.dsl.the
import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry

private const val ACCESS_WIDEN_MODEL = "accessWidenModel"

internal data class AccessWidenModel(
    val hash: Hash,
    val transformerEntries: Collection<ZipEntryTransformerEntry>
)

internal val Project.accessWidenModel
    get() = extra.getOrSet(ACCESS_WIDEN_MODEL) {
        provider {
            val awExt = the<AccessWidenExtension>()

            val aw = AccessWidener()
            val awReader = AccessWidenerReader(aw)

            val digest = MessageDigests.sha256()

            awExt.accessWideners.sorted().forEach { awFile ->
                awFile.bufferedReader().use(awReader::read)
                awFile.inputStream().use(digest::update)
            }

            val hash = Hash(digest.digest())

            val transformer = AccessWidenerZipEntryTransformer(aw)

            val entries = aw.targets.map { target ->
                val entryPath = target.replace('.', '/') + ".class"

                ZipEntryTransformerEntry(entryPath, transformer)
            }

            AccessWidenModel(hash, entries)
        }
    }
