# access-widen

A gradle plugin to apply [FabricMC's access wideners](https://wiki.fabricmc.net/tutorial:accesswideners) in any type of
project.

## Usage

access-widen is available in [gradle plugin portal](https://plugins.gradle.org/plugin/me.glicz.access-widen).

Simply add the plugin to your build file:

```kts
plugins {
    id("me.glicz.access-widen") version "..."
}
```

and configure it:

```kts
dependencies {
    accessWiden("com.example:example:1.0")
}

accessWiden {
    files.from(/*...*/)
    
    // optional, default: compileOnly
    outputConfigurations = setOf(
        ,
        "testImplementation"
    )
}
```

## Examples

### [Ignite](https://github.com/vectrix-space/ignite) (with [paperweight userdev](https://github.com/PaperMC/paperweight))

```kts
plugins {
    id("io.papermc.paperweight.userdev") version "..."
    id("me.glicz.access-widen") version "..."
}

dependencies {
    paperweight.paperDevBundle("...")
    
    // other dependencies like ignite api or mixin
}

paperweight {
    addServerDependencyTo = setOf(
        configurations.accessWiden.get()
    )
}

accessWideners {
    // load *.accesswidener files from resources
    files.from(fileTree(sourceSets.main.get().resources.srcDirs.first()) {
        include("*.accesswidener")
    })
}
```
