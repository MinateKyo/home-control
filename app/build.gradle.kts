import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("gnag")
}
apply(from = "../scripts/jacoco.gradle.kts")

val defaultUrl: String? by rootProject.extra

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.andrewgiang.homecontrol"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue("string", "default_url", defaultUrl ?: "")
    }

    buildTypes {
        getByName("debug") {
            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}


dependencies {
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation(project(":assistantsdk"))
    implementation(Libs.coroutines_android)

    //Androidx Libraries
    implementation(Libs.navigation_fragment)
    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui)
    implementation(Libs.navigation_ui_ktx)
    //Core
    implementation(Libs.app_compat)
    implementation(Libs.core_ktx)
    implementation(Libs.legacy_support_v4)
    implementation(Libs.fragment)
    implementation(Libs.constraint_layout)
    implementation(Libs.recycler_view)
    //Room
    implementation(Libs.room)
    kapt(Libs.room_compiler)

    implementation(Libs.lifecycle_extensions)
    kapt(Libs.lifecycle_compiler)

    //Work manager
    implementation(Libs.work_manager)

    // Google Material
    implementation(Libs.google_material)

    //Timber
    implementation(Libs.timber)

    //Dagger
    implementation(Libs.dagger)
    kapt(Libs.dagger_compiler)
    kapt(Libs.dagger_processor)

    //Retrofit
    implementation(Libs.retrofit)
    implementation(Libs.retrofit_moshi)
    implementation(Libs.coroutines_retrofit_adapter)

    //Chuck
    debugImplementation(Libs.chuck_debug)
    releaseImplementation(Libs.chuck_no_op)

    //Conceal
    implementation(Libs.facebook_conceal)
    implementation(Libs.material_dialog)
    implementation(Libs.material_dialog_color_chooser)
    //Material Icons
    implementation(Libs.material_icons)

    //Testing
    testImplementation(TestLibs.junit)
    testImplementation(TestLibs.mockito_core)
    testImplementation(TestLibs.mockk)
    testImplementation(TestLibs.android_core_testing)
    testImplementation(TestLibs.kotlin_coroutines_core)
    androidTestImplementation(TestLibs.android_test_runner)
    androidTestImplementation(TestLibs.espresso_core)
}
androidExtensions {
    // Workaround for this issue https://youtrack.jetbrains.com/issue/KT-22213
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

gnag {

    isEnabled = true
    setFailOnError(true)

    github {
        repoName("andrewgiang/home-control")
    }

    detekt {
        setEnabled(true)
        reporterConfig(project.file("detekt.config.yml"))
    }
}