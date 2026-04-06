plugins {
    id("net.darkhax.mmc") version "26.1.1.5"
}

allprojects {
    gradle.projectsEvaluated {
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(listOf("-Xmaxerrs", "9999"))
        }
    }
}