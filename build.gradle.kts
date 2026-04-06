plugins {
    id("net.darkhax.mmc") version "26.1.1.6"
}

allprojects {
    gradle.projectsEvaluated {
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(listOf("-Xmaxerrs", "9999"))
        }
    }
}