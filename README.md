Bookshelf
=========
Bookshelf is a core mod that expands upon Forge and Minecraft by adding new utilities, hooks and events which are publicly available to every mod authors. 

Compilation
===========
It is easy to build Bookshelf from the repository by using Forge Gradle. Bookshelf makes use of the Forge Gradle Wrapper, which is included as part of the repository. To compile the mod, simply clone or download the repository and run the 'gradlew build' command from the command line, from within the project directory. An obfuscated jar and a dev jar will be generated in the build/libs folder. 

Dev Environment
===============
This project relies on core mod functionality. All core mod features will only function correctly if the mod is set up as one. The easiest way to use Bookshelf in your dev environment is to download the deobfuscated jar from our [CurseForge](http://minecraft.curseforge.com/projects/bookshelf/files) page. All uploads will have an attached deobfuscated jar that can be found under the '+1 More' button. The deobfuscated version of Bookshelf can be installed by placing it in the mods folder within your workspace. Alternatively, if you want to work off of the raw source for this mod, you will need to create a dummy jar, or you can simply add the following line your your VM arguments.

-Dfml.coreMods.load=net.darkhax.bookshelf.asm.BookshelfLoadingPlugin      

Events
======
[ItemEnchantedEvent](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/event/ItemEnchantedEvent.java)  

Hooks
=====
[Horse Armor](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/items/ItemHorseArmor.java)  
[Color any Item](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/command/CommandItemColor.java)  

Other Features
==============
[Modeled Armor](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/items/ItemModelledArmor.java)   
[Simplified Messages](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/common/network/AbstractMessage.java)   
[More Enchantment IDs](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/handler/EnchantmentListExpansionHandler.java)   
[Position Object](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/util/Position.java)   
[Vanilla Color Enum](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/util/VanillaColor.java)   
[Assorted Utilities](https://github.com/Darkhax-Minecraft/Bookshelf/blob/master/src/main/java/net/darkhax/bookshelf/util/Utilities.java)   


Credits
=======
[ChickenBones](https://twitter.com/ChickenBones2)  
[CyanideX](https://twitter.com/theCyanideX)  
[Darkhax](https://twitter.com/Darkh4x)  
[iLexiconn](https://twitter.com/iLexiconn)   
[lclc98](https://twitter.com/lclc98)  
[SanAndreasP](https://twitter.com/SanAndreasP)  
[VikeStep](https://twitter.com/VikeStep)   
