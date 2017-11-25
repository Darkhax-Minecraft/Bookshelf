/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.darkhax.bookshelf.registry.IVariant;
import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is a version of ItemFood that has support for sub items. Each meta entry can have
 * it's own food, saturation, and potion effects. It also has support for drinkable foods.
 */
public class ItemSubTypeEdible extends ItemFood implements IVariant {

    /**
     * An array of all meta info. Index is equal to meta value.
     */
    private final FoodMetaInfo[] info;

    /**
     * An array of all the variant names. Index is equal to meta value.
     */
    private final String[] variants;

    public ItemSubTypeEdible (FoodMetaInfo[] info) {

        super(0, 0, false);
        this.setHasSubtypes(true);
        this.info = info;
        this.variants = Arrays.stream(info).map(FoodMetaInfo::getName).toArray(String[]::new);
    }

    /**
     * Gets the food meta info for an item stack.
     *
     * @param stack The stack to get meta for.
     * @return The food meta for the passed stack.
     */
    private FoodMetaInfo getInfo (ItemStack stack) {

        return this.isValidMeta(stack.getMetadata()) ? this.info[stack.getMetadata()] : FoodMetaInfo.EMPTY;
    }

    @Override
    public int getMetadata (int damage) {

        return damage;
    }

    @Override
    public void onFoodEaten (ItemStack stack, World worldIn, EntityPlayer player) {

        final FoodMetaInfo info = this.getInfo(stack);

        if (!worldIn.isRemote && !info.getEffects().isEmpty() && MathsUtils.tryPercentage(info.getPotionChance())) {

            for (final PotionEffect effect : info.getEffects()) {

                player.addPotionEffect(new PotionEffect(effect));
            }
        }
    }

    @Override
    public EnumAction getItemUseAction (ItemStack stack) {

        return this.getInfo(stack).isDrink() ? EnumAction.DRINK : EnumAction.EAT;
    }

    @Override
    public int getHealAmount (ItemStack stack) {

        return this.getInfo(stack).getFood();
    }

    @Override
    public float getSaturationModifier (ItemStack stack) {

        return this.getInfo(stack).getSaturation();
    }

    @Override
    public String[] getVariant () {

        return this.variants;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems (CreativeTabs tab, NonNullList<ItemStack> subItems) {

        if (this.isInCreativeTab(tab)) {

            for (int meta = 0; meta < this.getVariant().length; meta++) {

                subItems.add(new ItemStack(this, 1, meta));
            }
        }
    }

    /**
     * This class is a basic POJO for holding meta data about a food.
     */
    public static class FoodMetaInfo {

        /**
         * The default empty food value.
         */
        public static FoodMetaInfo EMPTY = new FoodMetaInfo(-1, "invalid", 0, 0);

        private int meta;
        private String name;
        private int food;
        private int saturation;
        private boolean isDrink;
        private float potionChance;
        private List<PotionEffect> effects;

        public FoodMetaInfo (int meta, String name, int food, int saturation) {

            this(meta, name, food, saturation, false, 0f, new ArrayList<>());
        }

        public FoodMetaInfo (int meta, String name, int food, int saturation, boolean isDrink) {

            this(meta, name, food, saturation, isDrink, 0f, new ArrayList<>());
        }

        public FoodMetaInfo (int meta, String name, int food, int saturation, boolean isDrink, float potionChance, List<PotionEffect> effects) {

            this.meta = meta;
            this.name = name;
            this.food = food;
            this.saturation = saturation;
            this.isDrink = isDrink;
            this.potionChance = potionChance;
            this.effects = effects;
        }

        public int getMeta () {

            return this.meta;
        }

        public void setMeta (int meta) {

            this.meta = meta;
        }

        public int getFood () {

            return this.food;
        }

        public void setFood (int food) {

            this.food = food;
        }

        public int getSaturation () {

            return this.saturation;
        }

        public void setSaturation (int saturation) {

            this.saturation = saturation;
        }

        public float getPotionChance () {

            return this.potionChance;
        }

        public void setPotionChance (float potionChance) {

            this.potionChance = potionChance;
        }

        public List<PotionEffect> getEffects () {

            return this.effects;
        }

        public void setEffects (List<PotionEffect> effects) {

            this.effects = effects;
        }

        public String getName () {

            return this.name;
        }

        public void setName (String name) {

            this.name = name;
        }

        public boolean isDrink () {

            return this.isDrink;
        }

        public void setDrink (boolean isDrink) {

            this.isDrink = isDrink;
        }
    }
}