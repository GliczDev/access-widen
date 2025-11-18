import me.glicz.accesswiden.accessWidenModel
import me.glicz.accesswiden.util.*
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Provider
import org.zeroturnaround.zip.ZipUtil
import java.io.File

fun Project.accessWiden(notation: String) =
    accessWiden(notation as Any)

fun Project.accessWiden(dependencyNotation: Any) =
    accessWiden(provider { configurations.detachedConfiguration(dependencies.create(dependencyNotation)) })

@JvmName("accessWidenConfiguration")
fun Project.accessWiden(configuration: Configuration) =
    accessWiden(provider { configuration })

@JvmName("accessWidenConfiguration")
fun Project.accessWiden(provider: Provider<out Configuration>) =
    accessWiden(provider.map { it.resolve() })

@JvmName("accessWidenTask")
fun Project.accessWiden(task: Task) =
    accessWiden(provider { task })

@JvmName("accessWidenTask")
fun Project.accessWiden(provider: Provider<out Task>) =
    accessWiden(provider.map { it.outputs.files })

@JvmName("accessWidenFiles")
fun Project.accessWiden(files: Iterable<File>) =
    accessWiden(provider { files })

@JvmName("accessWidenFiles")
fun Project.accessWiden(provider: Provider<out Iterable<File>>) = files(provider.map { files ->
    val awModel = accessWidenModel.get()

    val cacheDir = rootDir.resolve(CACHE_DIR)

    val awChecksum = cacheDir.resolve("aw.checksum")
    val awExpectedHash = awChecksum.readTextIfExists()?.toHexByteArray()

    if (!awModel.hash.contentEquals(awExpectedHash)) {
        cacheDir.walkBottomUp().forEach(File::forceDelete)
        cacheDir.mkdirs()

        awChecksum.writeText(awModel.hash.toHexString())
    }

    val digest = MessageDigests.sha256()

    files.map { input ->
        val output = cacheDir.resolve("${digest.digest(input).toHexString()}.jar")

        val checksum = cacheDir.resolve("${output.name}.checksum")
        val expectedHash = checksum.readTextIfExists()?.toHexByteArray()

        if (!output.exists() || !expectedHash.contentEquals(digest.digest(output))) {
            output.forceDelete()

            ZipUtil.transformEntries(input, awModel.transformerEntries.toTypedArray(), output)

            checksum.writeText(digest.digest(output).toHexString())
        }

        output
    }
})
