// add the dependencies
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.5.0")

    implementation(project(":discord"))
    implementation(project(":api"))

    // hikari
    implementation("com.zaxxer:HikariCP:5.0.1")
    // reflections
    implementation("org.reflections:reflections:0.10.2")
    // holograms
    implementation("dev.jaims.hololib:gson:0.1.1")
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    // gson
    implementation("com.google.code.gson:gson:2.9.0")
    // paper
    implementation("io.papermc:paperlib:1.0.6")
    // commodore
    implementation("me.lucko:commodore:2.0")
    // mf libs
    implementation("dev.triumphteam:triumph-gui:3.1.2")
    //adventure
    implementation("net.kyori:adventure-platform-bukkit:4.0.0")
    //kotlin reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")

    // test
    testImplementation("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    testImplementation(project(":bukkit"))
}

tasks.shadowJar {
    val libsDir = "dev.jaims.moducore.libs"
    listOf(
        "dev.jaims.hololib",
        "com.github.kittinunf",
        "com.github.shynixn",
        "com.google",
        "com.mojang",
        "com.okkero",
        "com.zaxxer",
        "io",
        "javassist",
        "khttp",
        "kotlin",
        "kotlinx",
        "me.mattstudios",
        "me.lucko",
        "org.bstats",
        "org.intellij",
        "org.jetbrains",
        "org.json",
        "org.reflections",
        "org.yaml",
    ).forEach { groupName -> relocate(groupName, "${libsDir}.${groupName}") }
}

// TEMP FIX
tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.WARN
}