package com.github.lukebemish.clojurewrapper.wrappers;

import clojure.lang.IFn;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ItemWrapper {
    public static Item.Properties props(IFn getter) {
        return new ExtendedProps().durabilityOrElse((Integer) getter.invoke("max-damage",0))
                .setFireRes((Boolean) getter.invoke("fire-resistant",false))
                .stacksTo((Integer) getter.invoke("stack-size",64))
                .tab((CreativeModeTab) getter.invoke("tab",null))
                .rarity((Rarity) getter.invoke("rarity", Rarity.COMMON))
                .food((FoodProperties) getter.invoke("food",null));
    }

    private static class ExtendedProps extends Item.Properties {
        public ExtendedProps durabilityOrElse(int maxDamage) {
            return maxDamage==0?this:(ExtendedProps)this.durability(maxDamage);
        }
        public ExtendedProps setFireRes(boolean isRes) {
            return isRes? (ExtendedProps) this.fireResistant() :this;
        }
    }
}
