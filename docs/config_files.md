# Config Files
Bookshelf offers a JSON based configuration framework. The framework includes helpers for reading and writing config files on demand. It also offers support for comments, example values, and other meta data values for various properties. 

## Usage Guide
To create a new config you need to create a ConfigHandler. The ConfigHandler is responsible for reading, writing, and reloading your config data. The ConfigHandler is created using it's public constructor which accepts a logger for printing errors and debug info, the name for the config file, and an object containing the default structure of your config. By default only fields with the `@Expose` annotation will be included in the config file.

```java
public class ExampleMod implements ModInitializer {

    private final Logger log = LogManager.getLogger("Example Mod");
    
    @Override
    public void onInitialize () {
        
        final ConfigHandler<ConfigData> handler = new ConfigHandler<>(log, "examplemod", new ConfigData());
        final ConfigData data = handler.read();
        System.out.println(data.toString());
    }   
    
    static class ConfigData {
        
        @Expose
        public int tickCount = 5;
        
        @Expose
        public boolean enableSystem = true;

        @Override
        public String toString () {
            
            return "ConfigData [tickCount=" + tickCount + ", enableSystem=" + enableSystem + "]";
        }
    }
}
```

The above example will produce an `examplemod.json` file in the instance's config directory containing the following content.

```json
{
    "tickCount": 5,
    "enableSystem": true
}
```

## Advanced Usage
While the previous example works many developers and users prefer to have additional metadata in the config file to explain what it does. This is accomplished by using Property<T> rather than the direct type. The Property<T> class provides several helper methods for providing metadata that will be included in the JSON data. This can be used to write comments, include default values, specify example values, and other properties. The order of these properties in the JSON will match the order that they are called.

```java
public class ExampleMod implements ModInitializer {

    private final Logger log = LogManager.getLogger("Example Mod");
    
    @Override
    public void onInitialize () {
        
        final ConfigHandler<ConfigData> handler = new ConfigHandler<>(log, "examplemod", new ConfigData());
        final ConfigData data = handler.read();
        System.out.println(data.toString());
    }   
    
    static class ConfigData {
        
        @Expose
        public Property<Integer> tickCount = new Property<>(5).comment("The amount of ticks before the mod does the thing that it does.").example(25).defaultValue();
        
        @Expose
        public Property<Boolean> enableSystem = new Property<>(true).comment("Is this mod currently enabled?").booleanValues().defaultValue();

        @Override
        public String toString () {
            
            return "ConfigData [tickCount=" + tickCount.get() + ", enableSystem=" + enableSystem.get() + "]";
        }
    }
}
```

The above example will produce the following JSON file. 
```json
{
  "tickCount": {
    "value": 5,
    "meta": {
      "comment": "The amount of ticks before the mod does the thing that it does.",
      "example": 25.0,
      "default": 5.0
    }
  },
  "enableSystem": {
    "value": true,
    "meta": {
      "comment": "Is this mod currently enabled?",
      "valid_values": [
        true,
        false
      ],
      "default": true
    }
  }
}
```

## Additional Notes
- This framework relies on GSON's object serialization. Any object that can be serialized using GSON can be used in the config.
- By default type adapters are included for common registry types like Item and Block which will serialize these values by their registry ID.
- Only fields with the `@Expose` annotation are included by default.
- You do not need to keep the reference to ConfigHandler if you don't want or need it.
- Multiline comments are supported. Strings longer than 80 characters will be wrapped automatically. If there is more than one line it will be depicted as an array.
- Custom meta properties can be added using `Property#getMeta`.
