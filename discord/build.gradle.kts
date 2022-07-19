dependencies {
    compileOnly(project(":api"))
}

// https://docs.gradle.org/7.0.2/userguide/validation_problems.html#implicit_dependency
tasks.compileJava { mustRunAfter(project(":common").tasks.shadowJar) }
tasks.compileKotlin { mustRunAfter(project(":common").tasks.shadowJar) }
