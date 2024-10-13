plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

version = "0.0.1"
group = "top.fifthlight.touchcontroller"

base {
    archivesName = "TouchContoller-Proxy"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.slf4j.api)
}