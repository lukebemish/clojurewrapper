(ns com.github.lukebemish.clojurewrapper.impl.registries
  (:require [com.github.lukebemish.clojurewrapper.init :as init]
            [com.github.lukebemish.clojurewrapper.api.util.functional :as functional])
  (:import (net.minecraftforge.registries ForgeRegistries IForgeRegistry IForgeRegistryEntry)
           (net.minecraftforge.event RegistryEvent$Register)
           (net.minecraftforge.eventbus.api EventPriority)
           (net.minecraft.resources ResourceLocation)
           (com.github.lukebemish.clojureloader ClojureModLoadingContext)
           (net.minecraft.data BuiltinRegistries)
           (net.minecraft.core Registry)))

(defn register-objects [mapin]
  (doseq [keyval mapin]
    (let [^IForgeRegistry registry
          (case (first keyval)
            :blocks ForgeRegistries/BLOCKS
            :fluids ForgeRegistries/FLUIDS
            :items ForgeRegistries/ITEMS
            :mob-effects ForgeRegistries/MOB_EFFECTS
            :sound-events ForgeRegistries/SOUND_EVENTS
            :potions ForgeRegistries/POTIONS
            :enchantments ForgeRegistries/ENCHANTMENTS
            :entity-types ForgeRegistries/ENTITIES
            :block-entity-types ForgeRegistries/BLOCK_ENTITIES
            :particle-types ForgeRegistries/PARTICLE_TYPES
            :menus ForgeRegistries/CONTAINERS
            :motives ForgeRegistries/PAINTING_TYPES
            :recipe-serializers ForgeRegistries/RECIPE_SERIALIZERS
            :attributes ForgeRegistries/ATTRIBUTES
            :stat-types ForgeRegistries/STAT_TYPES
            :villager-professions ForgeRegistries/PROFESSIONS
            :poi-types ForgeRegistries/POI_TYPES
            :memory-module-types ForgeRegistries/MEMORY_MODULE_TYPES
            :sensor-types ForgeRegistries/SENSOR_TYPES
            :schedules ForgeRegistries/SCHEDULES
            :activities ForgeRegistries/ACTIVITIES
            :carvers ForgeRegistries/WORLD_CARVERS
            :features ForgeRegistries/FEATURES
            :chunk-statuses ForgeRegistries/CHUNK_STATUS
            :structure-features ForgeRegistries/STRUCTURE_FEATURES
            :blockstate-provider-types ForgeRegistries/BLOCK_STATE_PROVIDER_TYPES
            :foliage-placer-types ForgeRegistries/FOLIAGE_PLACER_TYPES
            :tree-decorator-types ForgeRegistries/TREE_DECORATOR_TYPES
            :biomes ForgeRegistries/BIOMES
            :data-serializers ForgeRegistries/DATA_SERIALIZERS
            :loot-modifier-serializers ForgeRegistries/LOOT_MODIFIER_SERIALIZERS
            :world-types ForgeRegistries/WORLD_TYPES
            :configured-carvers BuiltinRegistries/CONFIGURED_CARVER
            :configured-features BuiltinRegistries/CONFIGURED_FEATURE
            :configured-structure-features BuiltinRegistries/CONFIGURED_STRUCTURE_FEATURE
            :placed-feature BuiltinRegistries/PLACED_FEATURE
            :processor-lists BuiltinRegistries/PROCESSOR_LIST
            :structure-template-pools BuiltinRegistries/TEMPLATE_POOL
            :noise-generator-settings BuiltinRegistries/NOISE_GENERATOR_SETTINGS
            :noise-parameters BuiltinRegistries/NOISE
            :game-events Registry/GAME_EVENT
            :custom-stats Registry/STAT_TYPE
            :rule-tests Registry/RULE_TEST
            :pos-rule-tests Registry/POS_RULE_TEST
            :position-source-types Registry/POSITION_SOURCE_TYPE
            :villager-types Registry/VILLAGER_TYPE
            :recipe-types Registry/RECIPE_TYPE
            :loot-pool-entry-types Registry/LOOT_POOL_ENTRY_TYPE
            :loot-function-types Registry/LOOT_FUNCTION_TYPE
            :loot-condition-types Registry/LOOT_CONDITION_TYPE
            :loot-number-provider-types Registry/LOOT_NUMBER_PROVIDER_TYPE
            :loot-nbt-provider-types Registry/LOOT_NBT_PROVIDER_TYPE
            :loot-score-provider-types Registry/LOOT_SCORE_PROVIDER_TYPE
            :float-provider-types Registry/FLOAT_PROVIDER_TYPES
            :int-provider-types Registry/INT_PROVIDER_TYPES
            :height-provider-types Registry/HEIGHT_PROVIDER_TYPES
            :block-predicate-types Registry/BLOCK_PREDICATE_TYPES)]
      (cond
        (nil? registry) (.error init/logger "Tried to register to unknown registry {}" (str (first keyval)))
        (instance? IForgeRegistry registry) (.addGenericListener
                                              (.getModEventBus (ClojureModLoadingContext/get))
                                              (.getRegistrySuperType registry)
                                              EventPriority/NORMAL
                                              false
                                              RegistryEvent$Register
                                              (functional/consumer
                                                #(let [reg (.getRegistry ^RegistryEvent$Register %)]
                                                   (if (= reg registry)
                                                     (doseq [rlval (second keyval)]
                                                       (.register reg (.setRegistryName ^IForgeRegistryEntry (apply (second rlval) '()) ^ResourceLocation (first rlval))))))))
        (instance? Registry registry) (doseq [rlval (second keyval)]
                                        (Registry/register ^Registry registry ^ResourceLocation (first keyval) (apply (second rlval) '())))))))