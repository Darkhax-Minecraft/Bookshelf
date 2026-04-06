package net.darkhax.bookshelf.common.mixin.patch.loot;

import net.minecraft.world.level.storage.loot.LootDataType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LootDataType.class)
public class MixinLootDataType {

//    @Inject(method = "deserialize(Lnet/minecraft/resources/Identifier;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Ljava/util/Optional;", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;error()Ljava/util/Optional;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
//    private void onDeserialize(Identifier id, DynamicOps<?> ops, Object value, CallbackInfoReturnable<Optional<?>> cir, DataResult<?> result) {
//        // Allow bookshelf load conditions to be used on loot tables.
//        if (value instanceof JsonObject obj && !LoadConditions.canLoad(obj)) {
//            cir.setReturnValue(Optional.empty());
//            return;
//        }
//        // These conditions have been split up because IDEA thinks it will always be false.
//        // This is not the case, and is related to mixin shenanigans.
//        if ((Object) this == LootDataType.TABLE) {
//            if (value instanceof JsonObject && result.error().isEmpty()) {
//                final Object rst = result.result().orElse(null);
//                LootTable table = bookshelf$getLootTable(rst);
//                if (table != null) {
//                    LootModificationHandler.HANDLER.get().processLootTable(id, table);
//                }
//            }
//        }
//    }
//
//    @Nullable
//    @Unique
//    private static LootTable bookshelf$getLootTable(Object rst) {
//        // Under normal circumstances rst is always a LootTable but NeoForge has
//        // patched the code to use Optional<LootTable> instead so we need to
//        // check and resolve those as well.
//        LootTable table = null;
//        if (rst instanceof LootTable lt) {
//            table = lt;
//        }
//        else if (rst instanceof Optional<?> optionalObj && optionalObj.orElse(null) instanceof LootTable lt) {
//            table = lt;
//        }
//        return table;
//    }
}