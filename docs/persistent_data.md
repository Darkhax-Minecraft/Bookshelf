# Persistent Data
The persistent data feature allows mods to store additional NBT properties on entities. This data will be saved with their normal NBT data and will persist across game restarts and dimension changes. This custom data is only handled on the logic (server) thread and will not be synchronized with the client. In the case of player data this data will also persist across death and leaving/rejoing the game. 

## Usage Guide
Custom data is stored on entities using a namespaced ID map. Each ID is linked to a new compound NBT tag that you can use to store whatever you want. This data is acessed using the `getPersistentData` method in EntityHelper. If the tag does not already exist a blank one will be created. Alternatively you may pass in a functon that will be used to create your own default tag when one does not already exist.

In the following example the persistent data is used to store a number that is incremented when a player right clicks any item. If the player is sneaking the count will be decreased instead. 

```java
public class ExampleMod implements ModInitializer {

    private final Identifier DATA_ID = new Identifier("examplemod", "click_data");
    
    @Override
    public void onInitialize () {
        
        UseItemCallback.EVENT.register(this::onPlayerUseItem);
    }   
    
    private TypedActionResult<ItemStack> onPlayerUseItem(PlayerEntity player, World world, Hand hand) {
        
        if (player != null && !player.world.isClient) {
            
            final CompoundTag dataTag = EntityHelper.getPersistentData(player, DATA_ID);
            final int clickCount = dataTag.getInt("count") + (player.isSneaking() ? -1 : 1);
            dataTag.putInt("count", clickCount);
            player.sendMessage(new LiteralText("Clicks: " + clickCount), false);
            
        }

        return TypedActionResult.pass(ItemStack.EMPTY);
    }
}
```