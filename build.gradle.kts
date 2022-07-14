plugins {
    kotlin("jvm") version "1.7.0"
    id("org.cadixdev.licenser") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    // id "me.bristermitten.pdm" version "0.0.33"
}

// delete build in the root directory to keep it clean
gradle.buildFinished {
    project.buildDir.delete()
}

// subprojects setup
allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.cadixdev.licenser")
    apply(plugin = "com.github.johnrengelman.shadow")
    // apply plugin: "me.bristermitten.pdm"

    tasks.build {
        dependsOn(tasks.test)
        dependsOn(tasks.shadowJar)
    }

    // all subprojects have the same group/version as the main project
    group = "dev.jaims.moducore"

    // version
    val ver = "0.7.3"
    val buildNumber = System.getenv()["BUILD_NUMBER"]
    version = if (buildNumber == null) ver else "$ver-b$buildNumber"

    // shadow & set archive base name so only 1 jar generates
    tasks.jar {
        archiveBaseName.set("moducore-${projectDir.name}")
    }

    tasks.shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("moducore-${projectDir.name}")
    }

    // all projects have some shared dependencies & repositories
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.jaims.dev/repository/all/") } // all repositories
    }

    // dependencies required for all projects
    dependencies {
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib")

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    }

    // process resources to show the correct version
    tasks.processResources {
        with(copySpec()) {
            from("src/main/resources")
            expand("version" to project.version)
        }
    }

    // add license
    license {
        header(rootProject.file("HEADER.txt"))
        include("**/*.kt", "**/*.java")
        newLine(true)
    }


    // kotlin options
    tasks.compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    tasks.compileTestKotlin {
        kotlinOptions.jvmTarget = "16"
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}