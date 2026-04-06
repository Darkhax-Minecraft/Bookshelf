<!-- name-start -->

# Bookshelf [![CurseForge Project](https://img.shields.io/curseforge/dt/228525?logo=curseforge&label=CurseForge&style=flat-square&labelColor=2D2D2D&color=555555)](https://www.curseforge.com/minecraft/mc-mods/bookshelf) [![Modrinth Project](https://img.shields.io/modrinth/dt/uy4Cnpcm?logo=modrinth&label=Modrinth&style=flat-square&labelColor=2D2D2D&color=555555)](https://modrinth.com/mod/bookshelf-lib) [![Maven Project](https://img.shields.io/maven-metadata/v?style=flat-square&logoColor=D31A38&labelColor=2D2D2D&color=555555&label=Latest&logo=gradle&metadataUrl=https%3A%2F%2Fmaven.blamejared.com%2Fnet%2Fdarkhax%2Fbookshelf%2Fbookshelf-common-26.1.1%2Fmaven-metadata.xml)](https://maven.blamejared.com/net/darkhax/bookshelf)

<!-- name-end -->

<!-- description-start -->
This is the official GitHub repo for the Bookshelf mod. Bookshelf is a library mod that provides code, frameworks, and
utilities for other mods. Many mods make use of Bookshelf and are powered by its code. You can download this mod
from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bookshelf)
or [Modrinth](https://modrinth.com/mod/bookshelf-lib). Please report
issues [here](https://github.com/Darkhax-Minecraft/Bookshelf/issues).
<!-- description-end -->

<!-- maven-start -->

## Maven Dependency

This project is available on the [BlameJared Maven](https://maven.blamejared.com).

If you are using [Gradle](https://gradle.org) you can add the mod as a dependency by adding the following
to your `build.gradle` file.

```groovy
repositories {
    maven {
        url 'https://maven.blamejared.com'
    }
}

dependencies {
     // NeoForge
     implementation group: 'net.darkhax.bookshelf', name: 'bookshelf-neoforge-26.1.1', version: '26.1.1.0'
     // Fabric
     implementation group: 'net.darkhax.bookshelf', name: 'bookshelf-fabric-26.1.1', version: '26.1.1.0'
     // Common / MultiLoader / Vanilla / No Loader
     implementation group: 'net.darkhax.bookshelf', name: 'bookshelf-common-26.1.1', version: '26.1.1.0'
}
```

<!-- maven-end -->

<!-- sponsor-start -->

## Sponsors

[![](https://assets.blamejared.com/nodecraft/darkhax.jpg)](https://nodecraft.com/r/darkhax)
Bookshelf is proudly sponsored by Nodecraft! Play your favorite games with your friends using their high
performance game servers! Use code **[DARKHAX](https://nodecraft.com/r/darkhax)** for 30% off your first
month of service!

<!-- sponsor-end -->
