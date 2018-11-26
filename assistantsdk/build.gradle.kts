plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_6
    targetCompatibility = JavaVersion.VERSION_1_6
}
dependencies {
    implementation(Libs.retrofit)
    implementation(Libs.retrofit_moshi)
    implementation(Libs.coroutines_retrofit_adapter)
    implementation(Libs.coroutines_android)
}