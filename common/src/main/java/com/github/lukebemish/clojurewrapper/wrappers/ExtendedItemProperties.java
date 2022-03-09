package com.github.lukebemish.clojurewrapper.wrappers;

import clojure.lang.IFn;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ExtendedItemProperties extends Item.Properties {
    public static Item.Properties props(IFn getter) {
        return new ExtendedItemProperties().durabilityOrElse((Integer) getter.invoke("max-damage",0))
                .setFireRes((Boolean) getter.invoke("fire-resistant",false))
                .stacksTo((Integer) getter.invoke("stack-size",64))
                .tab((CreativeModeTab) getter.invoke("tab",null))
                .rarity((Rarity) getter.invoke("rarity", Rarity.COMMON))
                .food((FoodProperties) getter.invoke("food",null));
    }

    public ExtendedItemProperties durabilityOrElse(int maxDamage) {
        return maxDamage==0?this:(ExtendedItemProperties)this.durability(maxDamage);
    }

    public ExtendedItemProperties setFireRes(boolean isRes) {
        return isRes?(ExtendedItemProperties)this.fireResistant():this;
    }
}
