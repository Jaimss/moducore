plugins {
    id("org.jetbrains.dokka") version "1.4.20"
    id("maven-publish")
    id("java-library")
}

apply(plugin = "maven-publish")
apply(plugin = "org.jetbrains.dokka")

version = "0.7"

tasks.dokkaHtml {
    outputDirectory.set(file("$projectDir/docs"))
    moduleName.set("moducore-api")
    dokkaSourceSets {
        configureEach {
            samples.from(file("$projectDir/../example/src/main/kotlin"))
            samples.from(file("$projectDir/../bukkit/src/main/kotlin"))
            externalDocumentationLink {
                //url = uri("https://hub.spigotmc.org/javadocs/spigot/")
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenKt") {
            from(components["java"])
            // https://stackoverflow.com/questions/40268027/how-can-i-exclude-a-dependency-from-a-pom-built-by-the-gradle-maven-publishing-p
        }
    }
    repositories {
        mavenLocal()
        maven {
            val releasesRepoUrl = "https://repo.jaims.dev/repository/maven-releases/"
            val snapshotsRepoUrl = "https://repo.jaims.dev/repository/maven-snapshots/"
            url = if (version.toString().contains("SNAPSHOT")) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)

            credentials {
                username = if (project.hasProperty("SONATYPE_USR")) project.property("SONATYPE_USR").toString()
                else System.getenv("SONATYPE_USR")

                password = if (project.hasProperty("SONATYPE_PSW")) project.property("SONATYPE_PSW").toString()
                else System.getenv("SONATYPE_PSW")
            }
        }
    }
}

dependencies {
    api(project(":common"))

    compileOnlyApi("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    api("net.dv8tion:JDA:5.0.0-alpha.13") {
        exclude(module = "opus-java")
    }

    api("dev.jaims.hololib:core:0.1.1")
    api("com.okkero.skedule:skedule:1.2.6")

    // triumph
    api("me.mattstudios:triumph-config:1.0.5-SNAPSHOT")
}

tasks.publish {
    dependsOn(tasks.dokkaHtml)
    dependsOn(tasks.clean)
}

// https://docs.gradle.org/7.0.2/userguide/validation_problems.html#implicit_dependency
tasks.shadowJar { mustRunAfter(project(":common").tasks.shadowJar) }
