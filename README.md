# Bookshelf [![](http://cf.way2muchnoise.eu/228525.svg)](https://minecraft.curseforge.com/projects/bookshelf) [![](http://cf.way2muchnoise.eu/versions/228525.svg)](https://minecraft.curseforge.com/projects/bookshelf)

Bookshelf provides code, frameworks, and utilities that other mods can use. Many mods make use of Bookshelf and it's shared code to power their own mods.

## Why use a library mod?
Library mods like Bookshelf allow many seemingly unrelated mods to share parts of the same reusable code base. This reduces the amount of time and effort required to develop certain mods and features. The shared code is also tested in a wider range of circumstances and communities which can lead to less bugs and a lower performance impact.

## Feature List
- Improved registry manager.
- Tile Entity with Client Syncing
- [Serialization for JSON/Network/NBT](https://github.com/Darkhax-Minecraft/Bookshelf/blob/1.16.3/src/main/java/net/darkhax/bookshelf/serialization/Serializers.java)
- [Utility Functions](https://github.com/Darkhax-Minecraft/Bookshelf/tree/1.16.3/src/main/java/net/darkhax/bookshelf/util)
- [New Recipe Types](https://github.com/Darkhax-Minecraft/Bookshelf/wiki/Data-Packs#recipe-types)
- [New Ingredient Types](https://github.com/Darkhax-Minecraft/Bookshelf/wiki/Data-Packs#ingredients)
- [New Loot Conditions](https://github.com/Darkhax-Minecraft/Bookshelf/wiki/Data-Packs#loot-conditions)
- [New Loot Modifiers](https://github.com/Darkhax-Minecraft/Bookshelf/wiki/Data-Packs#global-loot-modifiers)
- [New Item Predicates](https://github.com/Darkhax-Minecraft/Bookshelf/wiki/Data-Packs#item-predicates)
- [Debug Commands](https://github.com/Darkhax-Minecraft/Bookshelf/wiki/Commands)

## Maven Dependency
If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to replace the version with the correct one. All versions can be viewed [here](https://maven.mcmoddev.com/net/darkhax/bookshelf/).
```
repositories {

    maven { url 'https://maven.mcmoddev.com' }
}

dependencies {

    // Example: compile "net.darkhax.bookshelf:Bookshelf-1.16.4:9.0.2"
    compile "net.darkhax.bookshelf:Bookshelf-MCVERSION:PUT_BOOKSHELH_VERSION_HERE"
}
```