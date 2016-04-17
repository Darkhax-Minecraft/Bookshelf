Bookshelf
=========
Bookshelf is a mod that expands upon Forge and Minecraft by adding new utilities, hooks and events which are publicly available to every mod authors. 

Compilation
===========
It is easy to build Bookshelf from the repository by using Forge Gradle. Bookshelf makes use of the Forge Gradle Wrapper, which is included as part of the repository. To compile the mod, simply clone or download the repository and run the 'gradlew build' command from the command line, from within the project directory. An obfuscated jar and a dev jar will be generated in the build/libs folder. 


##Dependency Management
If you are using [Maven](https://maven.apache.org/download.cgi) to manage your dependencies. Add the following into your `pom.xml` file. Make sure to replace the version with the correct one. All versions can be viewed [here](http://maven.rubbix.net/net/darkhax/bookshelf/Bookshelf/).
```
<repositories>
    <repository>
        <id>Rubbix.net</id>
        <url>http://maven.rubbix.net</url>
    </repository>
</repositories>

<dependency>
     <groupId>net.darkhax.bookshelf</groupId>
     <artifactId>Bookshelf</artifactId>
     <version>PUT_VERSION_HERE</version>
</dependency>
```

If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to replace the version with the correct one. All versions can be viewed [here](http://maven.rubbix.net/net/darkhax/bookshelf/Bookshelf/).
```
repositories {

    maven { url 'http://maven.rubbix.net' }
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