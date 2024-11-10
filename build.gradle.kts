plugins {
    alias(libs.plugins.fabric.loom).apply(false)
    alias(libs.plugins.jetbrains.kotlin.jvm).apply(false)
    alias(libs.plugins.jetbrains.kotlin.android).apply(false)
    alias(libs.plugins.android.library).apply(false)
}

subprojects {
    repositories {
        mavenCentral()
        google()
    }
}