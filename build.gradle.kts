plugins {
    alias(libs.plugins.fabric.loom).apply(false)
    alias(libs.plugins.jetbrains.kotlin.jvm).apply(false)
}

subprojects {
    repositories {
        mavenCentral()
    }
}