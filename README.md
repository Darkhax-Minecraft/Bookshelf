Bookshelf [![](http://cf.way2muchnoise.eu/228525.svg)](https://minecraft.curseforge.com/projects/bookshelf) [![](http://cf.way2muchnoise.eu/versions/228525.svg)](https://minecraft.curseforge.com/projects/bookshelf)
=========
Bookshelf is a mod that expands upon Forge and Minecraft by adding new utilities, hooks and events which are publicly available to mod authors. 

[![Nodecraft](https://i.imgur.com/sz9PUmK.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

Compilation
===========
It is easy to build Bookshelf from the repository by using Forge Gradle. Bookshelf makes use of the Forge Gradle Wrapper, which is included as part of the repository. To compile the mod, simply clone or download the repository and run the 'gradlew build' command from the command line, from within the project directory. An obfuscated jar and a dev jar will be generated in the build/libs folder. 

Dependency Management
=====================

If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to replace the version with the correct one. All versions can be viewed [here](http://maven.epoxide.org/net/darkhax/bookshelf/Bookshelf/).
```
repositories {

    maven { url 'http://maven.epoxide.org' }
}

dependencies {

    compile "net.darkhax.bookshelf:Bookshelf:PUT_VERSION_HERE"
}
```

Credits
=======
[ChickenBones](https://twitter.com/ChickenBones2)  
[CyanideX](https://twitter.com/theCyanideX)  
[Darkhax](https://twitter.com/Darkh4x)  
[iLexiconn](https://twitter.com/iLexiconn)   
[lclc98](https://twitter.com/lclc98)  
[SanAndreasP](https://twitter.com/SanAndreasP)  
[VikeStep](https://twitter.com/VikeStep)   
[jaredlll08](https://twitter.com/jaredlll08)  
[Vazkii](https://twitter.com/Vazkii)    
[Mezz](https://twitter.com/mezz_mc)
