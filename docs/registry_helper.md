# Registry Helper
The registry helper provides a streamlined way to register new content such as items and blocks with the game. The system was designed to be simple to use while offering a lot of useful features and quality of life improvements for devs.

## Usage Guide
To use the RegistryHelper you need to construct a new instance of the class. This is commonly done as a private final field in your ModInitializer class or another class loaded from there. Once you have done this you can register new content using the internal registry fields. When you are done adding content invoke the apply method which will finalize your changes to the game registries.

```java
public class ExampleMod implements ModInitializer {

    private final RegistryHelper registry = new RegistryHelper("modid").withItemGroup(() -> Items.BOOKSHELF);
    
    @Override
    public void onInitialize () {
        
        registry.items.register(new Item(new Item.Settings().maxCount(12)), "example_item_one");
        registry.items.createItem("example_item_two", new Item.Settings().maxCount(12));
        registry.items.createItem("example_item_three");
        registry.apply();
    }   
}
```

There are many helper methods and features throughout the registry helper. For example `RegistryHelper#withItemGroup` can be used to set a single creative tab for your mod. When the creative tab has been specified all items registered through the registry helper will automatically be put in that tab. There is also the `createItem` method in the item registry which can be used to quickly create basic items like the vanilla Paper or Stick item. You can find more helper methods by referring to the JavaDocs for this system.

A brief list of other features include
- Ability to get all registered values for your mod.
- Debug logs for what values are being registered.
- Log for how many things were registered and how long it took to register them.
- Registry listeners that apply new code every time you register an item.