task<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.isEnabled = false
        html.isEnabled = true
    }
    sourceDirectories.setFrom(
        files("${project.projectDir}/src/main/java")
    )
    classDirectories.setFrom(
        fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
            exclude(
                setOf(
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    "**/*Activity*.*",
                    "**/*Fragment*.*",
                    "com/andrewgiang/homecontrol/dagger/**"
                )
            )
        }
    )
    executionData.setFrom(
        fileTree("${project.buildDir}") {
            include("jacoco/testDebugUnitTest.exec")
        })
}