(ns com.github.lukebemish.clojurewrapper.impl.registries
  (:import (net.minecraft.core Registry)
           (net.minecraft.resources ResourceLocation)
           (net.minecraft.data BuiltinRegistries)))

(defn register [rname, ^ResourceLocation location, supplier]
  (let [^Registry registry
        (case rname
          :blocks Registry/BLOCK
          :fluids Registry/FLUID
          :items Registry/ITEM
          :mob-effects Registry/MOB_EFFECT
          :sound-events Registry/SOUND_EVENT
          :potions Registry/POTION
          :enchantments Registry/ENCHANTMENT
          :entity-types Registry/ENTITY_TYPE
          :block-entity-types Registry/BLOCK_ENTITY_TYPE
          :particle-types Registry/PARTICLE_TYPE
          :menus Registry/MENU
          :motives Registry/MOTIVE
          :recipe-serializers Registry/RECIPE_SERIALIZER
          :attributes Registry/ATTRIBUTE
          :stat-types Registry/STAT_TYPE
          :villager-professions Registry/VILLAGER_PROFESSION
          :poi-types Registry/POINT_OF_INTEREST_TYPE
          :memory-module-types Registry/MEMORY_MODULE_TYPE
          :sensor-types Registry/SENSOR_TYPE
          :schedules Registry/SCHEDULE
          :activities Registry/ACTIVITY
          :carvers Registry/CARVER
          :features Registry/FEATURE
          :chunk-statuses Registry/CHUNK_STATUS
          :structure-features Registry/STRUCTURE_FEATURE
          :blockstate-provider-types Registry/BLOCKSTATE_PROVIDER_TYPES
          :foliage-placer-types Registry/FOLIAGE_PLACER_TYPES
          :tree-decorator-types Registry/TREE_DECORATOR_TYPES
          :biomes BuiltinRegistries/BIOME
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
    (if (not (nil? registry)) (Registry/register registry location (apply supplier '())))))

(defn register-objects [mapin]
  (doseq [keyval mapin]
    (doseq [rlval (second keyval)]
      (register (first keyval) (first rlval) (second rlval)))))