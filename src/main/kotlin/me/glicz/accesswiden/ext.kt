import me.glicz.accesswiden.AccessWidenExtension
import me.glicz.accesswiden.util.AccessWidenerZipEntryTransformer
import net.fabricmc.accesswidener.AccessWidener
import net.fabricmc.accesswidener.AccessWidenerReader
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.zeroturnaround.zip.ZipUtil
import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry
import java.io.File

fun Project.accessWiden(notation: String) =
    accessWiden(provider { configurations.detachedConfiguration(dependencies.create(notation)) })

@JvmName("accessWidenConfiguration")
fun Project.accessWiden(provider: Provider<out Configuration>) =
    accessWiden(provider.map { it.resolve() })

@JvmName("accessWidenTask")
fun Project.accessWiden(provider: Provider<out Task>) =
    accessWiden(provider.map { it.outputs.files })

@JvmName("accessWidenFiles")
fun Project.accessWiden(provider: Provider<out Iterable<File>>) = files(provider.map { files ->
    val accessWidenExt = extensions.getByType<AccessWidenExtension>()

    val accessWidener = AccessWidener()
    val reader = AccessWidenerReader(accessWidener)

    accessWidenExt.accessWideners.forEach { awFile ->
        awFile.bufferedReader().use(reader::read)
    }

    val transformer = AccessWidenerZipEntryTransformer(accessWidener)

    val entries = accessWidener.targets
        .map { target ->
            val entryPath = target.replace(".", "/") + ".class"

            ZipEntryTransformerEntry(entryPath, transformer)
        }
        .toTypedArray()

    layout.buildDirectory.asFile.map { buildDir ->
        val tempDir = buildDir.resolve("tmp/accessWiden").apply {
            mkdirs()
        }

        files.map { input ->
            val output = tempDir.resolve("${input.name}")

            ZipUtil.transformEntries(input, entries, output)

            output
        }
    }
})
