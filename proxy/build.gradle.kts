plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

version = "0.0.1"
group = "top.fifthlight.touchcontroller"

base {
    archivesName = "TouchContoller-Proxy"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    compileOnly(libs.slf4j.api)
}