plugins {
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.lagradost:cloudstream3:1.0.0")
}

tasks.register<Jar>("buildCs3") {
    archiveBaseName.set("MyWordPressProvider")
    archiveExtension.set("cs3")
    from(sourceSets.main.get().output)
    manifest {
        attributes["Main-Class"] = "com.lagradost.MyWordPressProviderPlugin"
    }
}