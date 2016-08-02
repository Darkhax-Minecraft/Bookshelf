Bookshelf
=========
Bookshelf is a mod that expands upon Forge and Minecraft by adding new utilities, hooks and events which are publicly available to every mod authors. 

Dependencies and Legal
======================
While many mods use Bookshelf as a dependency, this may not be the right option for everyone. This project is licensed under the public domain. This means that if you see something useful, you may take that code and include it within your own project. While not required, a link back to the project, or credit on the taken code would be very appreciated. The project is also looking for contributions if you have some useful code that you would be willing to donate. We accept pull requests and offers to pick through existing code bases to find useful things. 

Compilation
===========
It is easy to build Bookshelf from the repository by using Forge Gradle. Bookshelf makes use of the Forge Gradle Wrapper, which is included as part of the repository. To compile the mod, simply clone or download the repository and run the 'gradlew build' command from the command line, from within the project directory. An obfuscated jar and a dev jar will be generated in the build/libs folder. 

Features
========
- BlockTileEntity - Sane alternative to vanilla's BlockContainer.
- GuiGraphicButton - Small 20x20 button that uses an image rather than text.
- OpenParticleDigging - Open wrapper for vanilla's block digging particle.
- Anvil Recipes - Easy way to create new recipes in the anvil. 
- SlotArmor - An actual Slot for armor. Vanilla uses anonymous inner classes.
- ItemBlockBasic - A basic implementation of ItemBlock for meta blocks.
- ItemMusicDist - Open wrapper for vanilla's ItemRecord.
- BlockStates - Contains a bunch of common and reusable block state properties.
- ColorObject - A holder for color data, with support for NBT, packets and more.
- EnchantmentObject - A holder for basic enchantment data.
- Milibucket - An enumeration of standard milibucket ratios.
- ModifierOperation - An enumeration of vanilla's attribute modifier types.
- MutableString - A string that is also mutable.
- VanillaColor - An enumeration of the 16 vanilla colors. With OreDict info. 
- WeightedSelector - A flexible system for creating weighted registries.
- PropertyObject - A generic block property that can hold any object.

Utilities
=========
- BannerUtils - Utilities for creating banner stacks and adding new banner patterns.
- BlockUtils - Utilities blocks such as `isFluid(Block)`.
- ClassUtils - Utilities for working with classes, such as `compareObjectToClass(Object, Class)`.
- CraftingUtils - Utilities for crafting recipes, such as `getShapedRecipes(ItemStack)`.
- EnchantmentUtils - Utilities for enchantment related things, such as `areEnchantmentsCompatable(Enchantment, Enchantment)`.
- EntityUtils - Utilities for working with entities, such as `areEntitiesCloseEnough(Entity, Entity, double)`.
- ItemStackUtils - Utilities for working with ItemStacks, such as `isValidStack(ItemStack)`.
- MathsUtils - Utilities for doing maths, such as `tryPercentage(double)`;
- ModUtils - Utilities for minecraft mods, such as `getModName(IForgeRegistryEntry.Impl<?>)`.
- NBTUtils - Utilities such as `writeInventoryToNBT(NBTTagCompound, InventoryBasic)`
- NumericUtils - Utilities for working with numbers, such as `isSpecialDay(int, int)`.
- OreDictUtils - Constant reference to all well known OreDict names.
- PlayerUtils - Utilities for working with players, such as `getItemCountInInv(EntityPlayer, Item)`.
- RenderUtils - Utilities for renders and particles, such as `drawBlockOverlay(float, float, float, TextureAtlasSprite, int, int, int, int)`
- SkullUtils - Utilities for creating player heads, such as `createSkull(EntityPlayer player)`
- TextUtils - Utilities for working with text, such as `wrapStringToList(String, int, boolean, List<String>)`



Dependency Management
=====================
If you are using [Maven](https://maven.apache.org/download.cgi) to manage your dependencies. Add the following into your `pom.xml` file. Make sure to replace the version with the correct one. All versions can be viewed [here](http://maven.rubbix.net/net/darkhax/bookshelf/Bookshelf/).
```
<repositories>
    <repository>
        <id>epoxide.xyz</id>
        <url>http://maven.epoxide.xyz</url>
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

    maven { url 'http://maven.epoxide.xyz' }
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
