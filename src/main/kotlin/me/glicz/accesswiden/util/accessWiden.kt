package me.glicz.accesswiden.util

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Provider
import org.zeroturnaround.zip.ZipUtil
import java.io.File

internal fun Project.accessWiden0(dependency: Any) =
    accessWiden0(provider = dependency as? Provider<*> ?: provider { dependency })

private fun Project.accessWiden0(provider: Provider<*>) = files(provider.map {
    accessWiden0(
        files = when (it) {
            is Task -> it.outputs.files
            else -> (it as? Configuration ?: detachedConfiguration(it)).resolve()
        }
    )
})

private fun Project.accessWiden0(files: Iterable<File>): Iterable<File> {
    val awModel = accessWidenModel.get()

    val cacheDir = rootDir.resolve(CACHE_DIR)

    val awChecksum = cacheDir.resolve("aw.checksum")
    val awExpectedHash = awChecksum.readTextIfExists()?.let(::Hash)

    if (awModel.hash != awExpectedHash) {
        cacheDir.walkBottomUp().forEach(File::forceDelete)
        cacheDir.mkdirs()

        awChecksum.writeText(awModel.hash.toString())
    }

    val digest = MessageDigests.sha256()

    return files.map { input ->
        val output = cacheDir.resolve("${Hash(digest.digest(input))}.jar")

        val checksum = cacheDir.resolve("${output.name}.checksum")
        val expectedHash = checksum.readTextIfExists()?.let(::Hash)

        if (!output.exists() || Hash(digest.digest(output)) != expectedHash) {
            output.forceDelete()

            ZipUtil.transformEntries(input, awModel.transformerEntries.toTypedArray(), output)

            checksum.writeText(Hash(digest.digest(output)).toString())
        }

        output
    }
}
