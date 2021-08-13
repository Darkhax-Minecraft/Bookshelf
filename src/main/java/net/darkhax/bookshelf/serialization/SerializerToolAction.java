package net.darkhax.bookshelf.serialization;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import net.darkhax.bookshelf.Bookshelf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class SerializerToolAction implements ISerializer<ToolAction> {

    public static final ISerializer<ToolAction> SERIALIZER = new SerializerToolAction();

    private SerializerToolAction () {

    }
    
    @Override
    public ToolAction read (JsonElement json) {
        
        if (json.isJsonPrimitive()) {
            
            final String actionName = json.getAsString();
            return getAction(actionName);
        }
        
        throw new JsonParseException("Expected a string. Got " + GsonHelper.getType(json));
    }

    @Override
    public JsonElement write (ToolAction toWrite) {
        
        return new JsonPrimitive(toWrite.name());
    }

    @Override
    public ToolAction read (FriendlyByteBuf buffer) {
        
        final String actionName = buffer.readUtf();
        return getAction(actionName);
    }

    @Override
    public void write (FriendlyByteBuf buffer, ToolAction toWrite) {
        
        buffer.writeUtf(toWrite.name());
    }
    
    private ToolAction getAction(String name) {
        
        final Map<String, ToolAction> actions = ObfuscationReflectionHelper.getPrivateValue(ToolAction.class, null, "actions");
        
        if (!actions.containsKey(name)) {
            
            Bookshelf.LOG.warn("Could not find ToolAction with name {}. It will be created.");
        }
        
        return ToolAction.get(name);
    }
}