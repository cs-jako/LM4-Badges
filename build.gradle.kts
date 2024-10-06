plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "net.crazy"
version = "1.5.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

labyMod {
    defaultPackageName = "net.crazy.badges" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "badges"
        displayName = "Badges"
        author = "CrazySchnetzler"
        description = "Shows Badges of players from Laby.net above their NameTag."
        minecraftVersion = "*"
        iconUrl = "https://cs-jako.github.io/images/addons/Badges.png"
        version = "1.5.0"
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}