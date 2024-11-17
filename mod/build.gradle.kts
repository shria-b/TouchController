plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

version = "0.0.1"
group = "top.fifthlight.touchcontroller"

var modName = "TouchController"
base {
    archivesName = modName
}

fun DependencyHandlerScope.includeAndImplementation(dependencyNotation: Any) {
    include(dependencyNotation)
    implementation(dependencyNotation)
}

dependencies {
    minecraft(libs.minecraft)
    mappings("net.fabricmc:yarn:${libs.versions.yarn.get()}:v2")
    modImplementation(libs.fabric.loader)

    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.language.kotlin)
    modImplementation(libs.modmenu)
    modImplementation(libs.yacl)

    includeAndImplementation(project(":proxy-client"))
    includeAndImplementation(project(":proxy-server"))
    includeAndImplementation(libs.koin.core)
    includeAndImplementation(libs.koin.logger.slf4j)
    includeAndImplementation(libs.kotlinx.collections.immutable)
}

tasks.withType<ProcessResources> {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "version" to project.version,
            "name" to modName,
            "version_yacl" to libs.versions.yacl.get(),
        ))
    }
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Jar> {
    from(File(rootDir, "LICENSE")) {
        rename { "${it}_${modName}"}
    }
}
