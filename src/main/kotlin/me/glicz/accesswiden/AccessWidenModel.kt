package me.glicz.accesswiden

import me.glicz.accesswiden.util.AccessWidenerZipEntryTransformer
import me.glicz.accesswiden.util.Hash
import me.glicz.accesswiden.util.MessageDigests
import me.glicz.accesswiden.util.update
import net.fabricmc.accesswidener.AccessWidener
import net.fabricmc.accesswidener.AccessWidenerReader
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.internal.extensions.core.extra
import org.gradle.kotlin.dsl.getByType
import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry

private const val ACCESS_WIDEN_MODEL = "accessWidenModel"

data class AccessWidenModel(
    val hash: Hash,
    val transformerEntries: Collection<ZipEntryTransformerEntry>
)

val Project.accessWidenModel: Provider<AccessWidenModel>
    get() {
        if (extra.has(ACCESS_WIDEN_MODEL)) {
            @Suppress("UNCHECKED_CAST")
            return extra[ACCESS_WIDEN_MODEL] as Provider<AccessWidenModel>
        }

        val provider = provider {
            val awExt = extensions.getByType<AccessWidenExtension>()

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
                val entryPath = target.replace(".", "/") + ".class"

                ZipEntryTransformerEntry(entryPath, transformer)
            }

            AccessWidenModel(hash, entries)
        }

        return provider.also { extra[ACCESS_WIDEN_MODEL] = it }
    }
