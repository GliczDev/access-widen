plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.0.0"
    id("com.gradleup.shadow") version "8.3.9"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

dependencies {
    implementation("net.fabricmc:access-widener:2.1.0")
    implementation("org.ow2.asm:asm:9.9")
    implementation("org.ow2.asm:asm-commons:9.9")
    implementation("org.zeroturnaround:zt-zip:1.17")
    implementation("org.slf4j:slf4j-nop:1.6.6") // match the version zt-zip provides
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
