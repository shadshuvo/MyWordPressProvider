plugins {
    kotlin("jvm") version "1.9.10" // Update to the latest version
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

tasks.register<Jar>("buildCs3") {
    archiveBaseName.set("MyWordPressProvider")
    archiveExtension.set("cs3")
    from(sourceSets.main.get().output)
    dependsOn("build")
}