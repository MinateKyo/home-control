buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0-alpha07")
        classpath(kotlin("gradle-plugin", "1.3.10"))
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0-alpha07")
        classpath("org.jacoco:org.jacoco.core:0.8.2")
        classpath("com.btkelly:gnag:2.1.1")
    }
}

allprojects {

    repositories {
        google()
        jcenter()
        maven {
            setUrl("https://jitpack.io")
        }
        maven {
            setUrl("https://dl.bintray.com/drummer-aidan/maven/")
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}