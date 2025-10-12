plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.0"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

dependencies {
    implementation("net.fabricmc:access-widener:2.1.0")
    implementation("org.ow2.asm:asm:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")
    implementation("org.zeroturnaround:zt-zip:1.17")
}

kotlin {
    jvmToolchain(8)
}

gradlePlugin {
    website = "https://github.com/GliczDev/access-widen"
    vcsUrl = "https://github.com/GliczDev/access-widen"

    plugins {
        create("access-widen") {
            id = "$group.access-widen"
            implementationClass = "$group.accesswiden.AccessWidenPlugin"
            displayName = "access-widen"
            description = "A gradle plugin to apply FabricMC's access wideners"
            tags = listOf("access-widen", "access-widener", "fabric", "fabricmc")
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier = null

        listOf(
            "net.fabricmc",
            "org.objectweb",
            "org.slf4j",
            "org.zeroturnaround"
        ).forEach {
            relocate(it, "${project.group}.accesswiden.libs.$it")
        }
    }
}
