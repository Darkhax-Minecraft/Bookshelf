<!-- name-start -->
# Bookshelf [![CurseForge Project](https://img.shields.io/curseforge/dt/228525?logo=curseforge&label=CurseForge&style=flat-square&labelColor=2D2D2D&color=555555)](https://www.curseforge.com/minecraft/mc-mods/bookshelf) [![Modrinth Project](https://img.shields.io/modrinth/dt/uy4Cnpcm?logo=modrinth&label=Modrinth&style=flat-square&labelColor=2D2D2D&color=555555)](https://modrinth.com/mod/bookshelf-lib) [![Maven Project](https://img.shields.io/maven-metadata/v?style=flat-square&logoColor=D31A38&labelColor=2D2D2D&color=555555&label=Latest&logo=gradle&metadataUrl=https%3A%2F%2Fmaven.blamejared.com%2Fnet%2Fdarkhax%2Fbookshelf%2Fbookshelf-common-1.21%2Fmaven-metadata.xml)](https://maven.blamejared.com/net/darkhax/bookshelf)
<!-- name-end -->
<!-- description-start -->
Bookshelf is a library mod that provides code, frameworks, and utilities for other mods. Many mods make use of Bookshelf and are powered by its code. The documentation for this mod can be found [here](https://docs.darkhax.net/mods/bookshelf).
<!-- description-end -->

## Why use a library mod?
Library mods like Bookshelf allow seemingly unrelated mods to reuse parts
of the same code base. This reduces the amount of time required to develop
and maintain certain mods and features. Library code is also tested in a
wider range of circumstances and communities which can lead to fewer bugs
and faster code.

## Built With Bookshelf
The following mods were built using Bookshelf and are powered by its code!

- [Enchantment Descriptions](https://www.curseforge.com/minecraft/mc-mods/enchantment-descriptions) - Adds in-game descriptions for enchantments to tooltips.
- [Botany Pots](https://www.curseforge.com/minecraft/mc-mods/botany-pots) - Adds pots that you can use to grow crops!
- [Tips](https://www.curseforge.com/minecraft/mc-mods/tips) - Adds tips to various loading screens.
- [Dark Utilities](https://www.curseforge.com/minecraft/mc-mods/dark-utilities) - Blocks and items with interesting effects and abilities.

<!-- maven-start -->
## Maven Dependency

If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to replace the version with the correct one. All versions can be viewed [here](https://maven.blamejared.com/net/darkhax/bookshelf).

```gradle
repositories {
    maven { 
        url 'https://maven.blamejared.com'
    }
}

dependencies {
    // NeoForge
    implementation group: 'net.darkhax.bookshelf' name: 'bookshelf-neoforge-1.21' version: '21.0.0'

    // Forge
    implementation group: 'net.darkhax.bookshelf' name: 'bookshelf-forge-1.21' version: '21.0.0'

    // Fabric & Quilt
    modImplementation group: 'net.darkhax.bookshelf' name: 'bookshelf-fabric-1.21' version: '21.0.0'

    // Common / MultiLoader / Vanilla
    compileOnly group: 'net.darkhax.bookshelf' name: 'bookshelf-common-1.21' version: '21.0.0'
}
```
<!-- maven-end -->

<!-- sponsor-start -->
## Sponsors

[![](https://assets.blamejared.com/nodecraft/darkhax.jpg)](https://nodecraft.com/r/darkhax)    
Bookshelf is sponsored by Nodecraft. Use code **[DARKHAX](https://nodecraft.com/r/darkhax)** for 30% of your first month of service!
<!-- sponsor-end -->