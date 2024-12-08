# access-widen

A gradle plugin to apply [FabricMC's access wideners](https://wiki.fabricmc.net/tutorial:accesswideners) in any type of
project.

## Usage

access-widen is available in [gradle plugin portal](https://plugins.gradle.org/plugin/io.github.gliczdev.access-widen).

Simply add the plugin to your build file:

```kts
plugins {
    id("io.github.gliczdev.access-widen") version "..."
}
```

and configure it:

```kts
dependencies {
    accessWiden("com.example:example:1.0")
}

accessWideners {
    files.from(/*...*/)
}
```

then, to finalize your setup run:

```shell
./gradlew applyAccessWideners
```

## Examples

### [Ignite](https://github.com/vectrix-space/ignite) (with [paperweight userdev](https://github.com/PaperMC/paperweight))

```kts
plugins {
    id("io.papermc.paperweight.userdev") version "..."
    id("io.github.gliczdev.access-widen") version "..."
}

val paperVersion = "..."

dependencies {
    paperweight.paperDevBundle(paperVersion)

    // other dependencies like ignite api or mixin

    accessWiden("io.papermc.paper:paper-server:userdev-$paperVersion")
}

accessWiden {
    // load *.accesswidener files from resources
    files.from(fileTree(sourceSets.main.get().resources.srcDirs.first()) {
        include("*.accesswidener")
    })
}

tasks {
    compileJava {
        dependsOn(applyAccessWideners)
    }
}
```
