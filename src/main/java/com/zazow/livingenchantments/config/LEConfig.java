package com.zazow.livingenchantments.config;

import com.electronwill.nightconfig.core.Config;
import com.mojang.datafixers.util.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;

public class LEConfig {
    public static final ForgeConfigSpec.Builder GENERAL_BUILDER = new ForgeConfigSpec.Builder();

    public static final General GENERAL = new General(GENERAL_BUILDER);


    public static final ForgeConfigSpec GENERAL_SPEC = GENERAL_BUILDER.build();
    public static class General {

        public final ForgeConfigSpec.DoubleValue talentChance;
        public final ForgeConfigSpec.DoubleValue legendaryTalentChanceOverride;
        public final ForgeConfigSpec.ConfigValue<List<Integer>> talentAwakeningLevels;
        public final ForgeConfigSpec.ConfigValue<List<Integer>> veinMinerMaxBlocks;
        public final ForgeConfigSpec.ConfigValue<List<Integer>> gridMiningSizes;

        public final ForgeConfigSpec.ConfigValue<List<Double>> thornsDamageMultipliers;
        public final ForgeConfigSpec.ConfigValue<List<Double>> thornsBlockedDamageMultipliers;
        public final ForgeConfigSpec.ConfigValue<List<Double>> lifeLeechDamageFractions;
        public final ForgeConfigSpec.ConfigValue<List<Double>> lifeLeechBlockFractions;

        public final ForgeConfigSpec.ConfigValue<List<Double>> feedOdds;
        public final ForgeConfigSpec.ConfigValue<List<Double>> feedDamageFractions;
        public final ForgeConfigSpec.ConfigValue<List<Double>> saturationDamageFractions;
        public final ForgeConfigSpec.ConfigValue<List<Double>> tempoMovementSpeedMultipliers;
        public final ForgeConfigSpec.ConfigValue<List<Double>> luckTalentValues;
        public final ForgeConfigSpec.ConfigValue<List<Double>> meditationFixPercentages;
        public final ForgeConfigSpec.ConfigValue<List<Double>> saviorChanceOdds;
        public final ForgeConfigSpec.ConfigValue<List<Double>> vitalityTalentValues;

        public final ForgeConfigSpec.ConfigValue<Double> protectorRange;






        public final ForgeConfigSpec.DoubleValue legendaryOddsCutoff;
        public final ForgeConfigSpec.DoubleValue epicOddsCutoff;
        public final ForgeConfigSpec.DoubleValue rareOddsCutoff;
        public final ForgeConfigSpec.DoubleValue uncommonOddsCutoff;

        public final ForgeConfigSpec.IntValue durabilityWarningWait;

        public final ForgeConfigSpec.IntValue talkWait;


        // Xp
        Map<String, Double> entityDamageXpModifierCache;
        public Map<String, Double> getEntityDamageXpModifier() {
            if (entityDamageXpModifierCache != null) {
                return entityDamageXpModifierCache;
            }
            entityDamageXpModifierCache = new HashMap<>();

            for (String s : entityDamageXpModifier.get()) {
                String[] vals = s.split(",");
                if (vals.length != 2) {
                    continue;
                }
                try {
                    double val = Double.parseDouble(vals[1]);
                    entityDamageXpModifierCache.put(vals[0], val);
                } catch (NumberFormatException e) {
                }
            }

            return entityDamageXpModifierCache;
        }
        public final ForgeConfigSpec.ConfigValue<List<String>> entityDamageXpModifier;

        public final ForgeConfigSpec.DoubleValue blockBreakBaseXpReward;
        public final ForgeConfigSpec.DoubleValue blockHardnessXpMultiplier;
        public final ForgeConfigSpec.DoubleValue damageTakenBaseXpReward;

        public final ForgeConfigSpec.DoubleValue damageTakenXpMultiplier;
        public final ForgeConfigSpec.DoubleValue damageDealtBaseXpReward;

        public final ForgeConfigSpec.DoubleValue damageDealtXpMultiplier;
        public final ForgeConfigSpec.DoubleValue killingBaseXpReward;

        public final ForgeConfigSpec.DoubleValue killedEntityHealthXpMultiplier;
        public final ForgeConfigSpec.DoubleValue blockingBaseXpReward;

        public final ForgeConfigSpec.DoubleValue hoeBaseXpReward;

        public final ForgeConfigSpec.DoubleValue damageBlockedXpMultiplier;

        public final ForgeConfigSpec.DoubleValue globalEffectivenessMultiplier;
        public final ForgeConfigSpec.DoubleValue damageEffectivenessMultiplier;
        public final ForgeConfigSpec.DoubleValue armorEffectivenessMultiplier;
        public final ForgeConfigSpec.DoubleValue breakSpeedEffectivenessMultiplier;
        public final ForgeConfigSpec.DoubleValue attackSpeedEffectivenessMultiplier;

        General(ForgeConfigSpec.Builder builder) {
            String desc;

            builder.push("Talents");

            desc = "Chance for item to get a talent.";
            talentChance = builder.comment(desc).defineInRange("talentChance", 0.3, 0, 1);

            desc = "Chance for item to get a talent if it has a legendary personality.";
            legendaryTalentChanceOverride = builder.comment(desc).defineInRange("legendaryTalentChanceOverride", 1.d, 0, 1);

            desc = "The levels at which a living tool's talent awakens";
            talentAwakeningLevels = builder.comment(desc).define("talentAwakeningLevels",  Arrays.stream((new Integer[]{10, 20, 30, 40, 50})).toList());

            desc = "Max mined blocks for vein mining. Values are interpolated between levels";
            veinMinerMaxBlocks = builder.comment(desc).define("veinMinerMaxBlocks",  Arrays.stream((new Integer[]{3, 6, 9, 16, 20})).toList());
            desc = "Grid size to use for grid miner. Values are NOT interpolated between levels";
            gridMiningSizes = builder.comment(desc).define("gridMiningSizes",  Arrays.stream((new Integer[]{3, 3, 3, 5, 5})).toList());

            desc = "Thorns damage fractions done back to attacker. Based on talentAwakeningLevels.";
            thornsDamageMultipliers = builder.comment(desc).define("thornsDamageMultipliers",  Arrays.stream((new Double[]{0.1, 0.2, 0.3, 0.4, 0.5})).toList());
            desc = "Thorns damage fractions done to back to attacker when damage is blocked. Based on talentAwakeningLevels.";
            thornsBlockedDamageMultipliers = builder.comment(desc).define("thornsBlockedDamageMultipliers",  Arrays.stream((new Double[]{0.15, 0.3, 0.45, 0.6, 0.8})).toList());

            desc = "Heal done to the player as fraction of damage dealt. Value for each talentAwakeningLevels.";
            lifeLeechDamageFractions = builder.comment(desc).define("lifeLeechDamageFractions", Arrays.stream((new Double[]{0.05, 0.1, 0.15, 0.2, 0.3})).toList());
            desc = "Heal done to the player as fraction of damage blocked (shields not armor). Value for each talentAwakeningLevels.";
            lifeLeechBlockFractions = builder.comment(desc).define("lifeLeechBlockFractions", Arrays.stream((new Double[]{0.08, 0.12, 0.18, 0.23, 0.4})).toList());

            desc = "Chance to give food to the player as fraction of damage dealt. Value for each talentAwakeningLevels.";
            feedOdds = builder.comment(desc).define("feedOdds",  Arrays.stream((new Double[]{0.05, 0.1, 0.15, 0.2, 0.3})).toList());
            desc = "Food given to the player as fraction of damage dealt. Value for each talentAwakeningLevels.";
            feedDamageFractions = builder.comment(desc).define("feedDamageFractions", Arrays.stream((new Double[]{0.02, 0.05, 0.08, 0.12, 0.2})).toList());

            desc = "Saturation given to the player as fraction of food given because of damage. Value for each talentAwakeningLevels.";
            saturationDamageFractions = builder.comment(desc).define("saturationDamageFractions",  Arrays.stream((new Double[]{0.5})).toList());

            desc = "Speed increase as a percent to the player. Value for each talentAwakeningLevels.";
            tempoMovementSpeedMultipliers = builder.comment(desc).define("tempoSpeedMultipliers", Arrays.stream((new Double[]{0.1, 0.2, 0.4, 0.6, 0.8})).toList());

            desc = "Luck values given to player. Value for each talentAwakeningLevels.";
            luckTalentValues = builder.comment(desc).define("luckTalentValues", Arrays.stream((new Double[]{1.d, 1.5d, 2.d, 3.d, 5.d})).toList());

            desc = "Percent of tool fixed when the player sleeps. Value for each talentAwakeningLevels.";
            meditationFixPercentages = builder.comment(desc).define("luckTalentValues", Arrays.stream((new Double[]{0.05d, 0.065d, 0.8d, 0.1d, 0.13d})).toList());

            desc = "Chance for Savior Talent to save player from death. Value for each talentAwakeningLevels.";
            saviorChanceOdds = builder.comment(desc).define("saviorChanceOdds", Arrays.stream((new Double[]{0.5d})).toList());

            desc = "Extra health given to player. Value for each talentAwakeningLevels.";
            vitalityTalentValues = builder.comment(desc).define("vitalityTalentValues", Arrays.stream((new Double[]{2.D, 4.D, 6.D, 10.D, 20.D})).toList());

            desc = "The range of activation for Protector Talent. This will stop all mob griefing in an area around the player.";
            protectorRange = builder.comment(desc).defineInRange("saviorChanceOdds", 8.D, 0, 100000);

            builder.pop();
            builder.push("Personalities");

            desc = "The cutoff point for getting a legendary personality.";
            legendaryOddsCutoff = builder.comment(desc).defineInRange("legendaryOddsCutoff", 0.4d, 0, 1);
            desc = "The cutoff point for getting a epic personality.";
            epicOddsCutoff = builder.comment(desc).defineInRange("epicOddsCutoff", 0.13d, 0, 1);
            desc = "The cutoff point for getting a rare personality.";
            rareOddsCutoff = builder.comment(desc).defineInRange("rareOddsCutoff", 0.30d, 0, 1);;
            desc = "The cutoff point for getting a uncommon personality.";
            uncommonOddsCutoff = builder.comment(desc).defineInRange("uncommonOddsCutoff", 0.6d, 0, 1);

            desc = "Wait time between warning player about item durability in milliseconds.";
            durabilityWarningWait = builder.comment(desc).defineInRange("durabilityWarningWait", 10000, 0, 1000000);
            desc = "Wait time between chatting with the player in milliseconds.";
            talkWait = builder.comment(desc).defineInRange("talkWait", 60000, 0, 1000000);

            builder.pop();

            builder.push("Xp");

            List<String> entityDamageXpModifierMap = new ArrayList<>();
            entityDamageXpModifierMap.add("dannys_expansion:test_dummy, 0.1");
            desc = "Xp multiplier for specific damaged entities.";
            entityDamageXpModifier = builder.comment(desc).define("entityDamageXpModifier", entityDamageXpModifierMap, (list) -> {
                if (list == null) {
                    return false;
                }
                for (String s : (List<String>) list) {
                    String[] vals = s.split(",");
                    if (vals.length != 2) {
                        return false;
                    }
                    try {
                        Double.parseDouble(vals[1]);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                return true;
            });
            desc = "breaking block xp reward is blockBreakBaseXpReward + hardness * blockHardnessXpMultiplier.";
            blockBreakBaseXpReward = builder.comment(desc).defineInRange("blockBreakBaseXpReward", 1.d, 0, 100000);
            blockHardnessXpMultiplier = builder.comment(desc).defineInRange("blockHardnessXpMultiplier", 0.2d, 0, 1);
            desc = "damage taken xp reward is baseXpReward + damage_amount * damageTakenXpMultiplier.";
            damageTakenBaseXpReward = builder.comment(desc).defineInRange("damageTakenBaseXpReward", 1.d, 0, 100000);
            damageTakenXpMultiplier = builder.comment(desc).defineInRange("damageTakenXpMultiplier", 0.8d, 0, 1);
            desc = "damage dealt xp reward is baseXpReward + damage_amount * damageDealtXpMultiplier.";
            damageDealtBaseXpReward = builder.comment(desc).defineInRange("damageDealtBaseXpReward", 1.d, 0, 100000);
            damageDealtXpMultiplier = builder.comment(desc).defineInRange("damageDealtXpMultiplier", 0.4d, 0, 1);
            desc = "Xp reward for killing killing an entity is baseXpReward + entity_max_health * killedEntityHealthXpMultiplier.";
            killingBaseXpReward = builder.comment(desc).defineInRange("killingBaseXpReward", 1.d, 0, 100000);
            killedEntityHealthXpMultiplier = builder.comment(desc).defineInRange("killedEntityHealthXpMultiplier", 0.1d, 0, 1);
            desc = "blocking damage xp reward is baseXpReward + damage_blocked * damageBlockedXpMultiplier.";
            blockingBaseXpReward = builder.comment(desc).defineInRange("blockingBaseXpReward", 1.d, 0, 100000);
            damageBlockedXpMultiplier = builder.comment(desc).defineInRange("damageBlockedXpMultiplier", 0.4d, 0, 1);

            desc = "Base xp rewarded for tilling land.";
            hoeBaseXpReward = builder.comment(desc).defineInRange("hoeBaseXpReward", 3.d, 0, 100000);
            builder.pop();

            builder.push("General");
            desc = "Effectiveness = level * personality_effectiveness * globalEffectivenessMultiplier.";
            globalEffectivenessMultiplier = builder.comment(desc).defineInRange("globalEffectivenessMultiplier", 1.d, 0, 100000);
            desc = "Effectiveness is based on level and personality. This value determines how effectiveness should be used to modify damage.";
            damageEffectivenessMultiplier = builder.comment(desc).defineInRange("damageEffectivenessMultiplier", 0.1d, 0, 100000);
            desc = "Effectiveness is based on level and personality. This value determines how effectiveness should be used to modify armor.";;
            armorEffectivenessMultiplier = builder.comment(desc).defineInRange("armorEffectivenessMultiplier", 0.1d, 0, 100000);
            desc = "Effectiveness is based on level and personality. This value determines how effectiveness should be used to modify break speed.";
            breakSpeedEffectivenessMultiplier = builder.comment(desc).defineInRange("breakSpeedEffectivenessMultiplier", 0.5d, 0, 100000);
            desc = "Effectiveness is based on level and personality. This value determines how effectiveness should be used to modify attack speed.";
            attackSpeedEffectivenessMultiplier = builder.comment(desc).defineInRange("attackSpeedEffectivenessMultiplier", 0.05d, 0, 100000);
            builder.pop();
        }
    }
}
