plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

version = "0.0.1"
group = "top.fifthlight.touchcontroller.proxy.client"

base {
    archivesName = "TouchContoller-Proxy-Client"
}

dependencies {
    implementation(project(":proxy-server"))
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.slf4j.api)
    testImplementation(libs.logback.classic)
    testImplementation(libs.slf4j.api)
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}
