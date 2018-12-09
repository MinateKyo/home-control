object Versions {
    const val navigation = "1.0.0-alpha07"
    const val dagger = "2.19"
    const val retrofit = "2.4.0"
    const val room = "2.1.0-alpha02"
    const val lifecycle = "2.0.0"
}

object TestLibs {
    const val junit = "junit:junit:4.12"
    const val mockito_core = "org.mockito:mockito-core:2.23.0"
    const val mockk = "io.mockk:mockk:1.8.12.kotlin13"
    const val kotlin_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1"
    const val espresso_core = "androidx.test.espresso:espresso-core:3.1.0"
    const val android_core_testing = "android.arch.core:core-testing:1.1.1"
    const val android_test_runner = "androidx.test:runner:1.1.0"
}

object Libs {
    //Core android libraries
    const val navigation_fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
    const val navigation_fragment_ktx = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui = "android.arch.navigation:navigation-ui:${Versions.navigation}"
    const val navigation_ui_ktx = "android.arch.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val core_ktx = "androidx.core:core-ktx:1.0.1"
    const val app_compat = "androidx.appcompat:appcompat:1.0.2"
    const val legacy_support_v4 = "androidx.legacy:legacy-support-v4:1.0.0"
    const val fragment = "androidx.fragment:fragment:1.1.0-alpha01"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val recycler_view = "androidx.recyclerview:recyclerview:1.0.0"
    //Room
    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    //Lifecycle
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    //Work manager
    const val work_manager = "android.arch.work:work-runtime-ktx:1.0.0-alpha11"
    // Google Material
    const val google_material = "com.google.android.material:material:1.1.0-alpha01"

    //Timber
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    //Dagger
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val dagger_processor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    //Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    //Chuck
    const val chuck_debug = "com.readystatesoftware.chuck:library:1.1.0"
    const val chuck_no_op = "com.readystatesoftware.chuck:library-no-op:1.1.0"

    const val facebook_conceal = "com.facebook.conceal:conceal:2.0.1@aar"

    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1"
    const val coroutines_retrofit_adapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
    const val material_icons = "net.steamcrafted:materialiconlib:1.1.5"
    const val material_dialog = "com.afollestad.material-dialogs:core:2.0.0-rc3"
    const val material_dialog_color_chooser = "com.afollestad.material-dialogs:color:2.0.0-rc3"
    const val klaster = "com.github.rongi:klaster:0.3.3"
}
