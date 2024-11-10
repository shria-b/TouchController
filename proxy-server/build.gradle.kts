plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

version = "0.0.1"
group = "top.fifthlight.touchcontroller.proxy.server"

base {
    archivesName = "TouchContoller-Proxy-Server"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    compileOnly(libs.slf4j.api)
}

kotlin {
    jvmToolchain(8)
}
