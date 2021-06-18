# Bookshelf Wiki (Fabric)

Bookshelf provides code, frameworks, and utilities that other mods can use. Many mods make use of Bookshelf and it's shared code to power their own mods.

## Why use a library?
Library mods like Bookshelf allow many seemingly unrelated mods to share parts of the same reusable code base. This reduces the amount of time and effort required to develop certain mods and features. The shared code is also tested in a wider range of circumstances and communities which can lead to less bugs and a lower performance impact. 

## Feature List
- Registry helper
- Config System
- Persistent Player Data

## Maven Dependency
If you are using Gradle to manage your dependencies, add the following into your build.gradle file. Make sure to replace the version with the correct one. All versions can be viewed [here](https://maven.blamejared.com/net/darkhax/bookshelf/).

```groovy
repositories {

    maven { url 'https://maven.blamejared.com' }
}

dependencies {

    compile "net.darkhax.bookshelf:Bookshelf-Fabric-MCVERSION:PUT_BOOKSHELH_VERSION_HERE"
}
```