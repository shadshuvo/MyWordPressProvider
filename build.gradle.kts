plugins {
    kotlin("jvm") version "1.9.10" // Update to the latest version
}

repositories {
    mavenCentral()
    google() // For Android dependencies
    jcenter() // For additional dependencies
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.cloudstream3:cloudstream3:1.0.0") // Replace with the correct version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.json:json:20210307")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("androidx.core:core-ktx:1.6.0")
    testImplementation(kotlin("test"))
}

tasks.register<Jar>("buildCs3") {
    archiveBaseName.set("MyWordPressProvider")
    archiveExtension.set("cs3")
    from(sourceSets.main.get().output)
    dependsOn("build")
}