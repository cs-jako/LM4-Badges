rootProject.name = "badges"

pluginManagement {
    val labyGradlePluginVersion = "0.5.5"
    buildscript {
        repositories {
            maven("https://dist.labymod.net/api/v1/maven/release/")
            maven("https://maven.neoforged.net/releases/")
            maven("https://maven.fabricmc.net/")
            gradlePluginPortal()
            mavenCentral()
        }

        dependencies {
            classpath("net.labymod.gradle", "common", labyGradlePluginVersion)
        }
    }
}

plugins.apply("net.labymod.labygradle.settings")

include(":api")
include(":core")