package com.zazow.livingenchantments.util;

import com.zazow.livingenchantments.attributes.Personality;
import com.zazow.livingenchantments.attributes.Talent;
import com.zazow.livingenchantments.config.LEConfig;
import com.zazow.livingenchantments.enchantments.EnchantmentRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class LEUtil {
    public static String PERSONALITY_KEY = "personality";
    public static String TALENT_KEY = "talent";
    public static String UUID_KEY = "uuid";
    public static final String USAGE_COUNT_KEY = "uses";
    public static final String XP_KEY = "xp";
    public static final String LAST_TALK_KEY = "lastTalk";
    public static CompoundTag getLivingEnchantmentTag(ListTag listTag) {
        for (Tag tag : listTag) {
            if (!(tag instanceof CompoundTag)) {
                continue;
            }
            CompoundTag compoundTag = (CompoundTag)tag;
            if (!compoundTag.getString("id").equals(EnchantmentRegistry.LIVING_ENCHANTMENT.getRegistryName().toString())) {
                continue;
            }
            return compoundTag;
        }
        return null;
    }
    public static CompoundTag getLivingEnchantmentTag(ItemStack stack) {
        return getLivingEnchantmentTag(stack.getEnchantmentTags());
    }

    private static double xpToLevelEquation(double xp) {
        return Math.sqrt(xp / 35.d);
    }

    private static double xpToLevelInverseEquation(double xp) {
        return xp * xp * 35.d;
    }

    public static double getItemXp(CompoundTag tag) {
        return tag.getDouble(XP_KEY);
    }
    public static int xpToLevel(double xp) {
        return (int) xpToLevelEquation(xp) + 1;
    }
    public static int getItemLevel(CompoundTag tag) {
        return xpToLevel(getItemXp(tag));
    }
    public static double xpNeededToNextLevel(double xp) {
        return xpToLevelInverseEquation(xpToLevel(xp));
    }
    public static double getItemXpToNextLevel(CompoundTag tag) {
        return xpNeededToNextLevel(getItemXp(tag));
    }

    public static boolean isToolEffective(DiggerItem item, BlockState state) {
        return state.is(item.blocks);
    }

    public static double getBreakSpeedMultiplier(CompoundTag tag) {
        return 1 + (getToolEffectivenessModifier(tag) * LEConfig.GENERAL.breakSpeedEffectivenessMultiplier.get());
    }
    public static double getAttackSpeedMultiplier(CompoundTag tag) {
        return getToolEffectivenessModifier(tag) * LEConfig.GENERAL.attackSpeedEffectivenessMultiplier.get();
    }
    public static double getDamageMultiplier(CompoundTag tag) {
        return getToolEffectivenessModifier(tag) * LEConfig.GENERAL.damageEffectivenessMultiplier.get();
    }
    public static double getArmorMultiplier(CompoundTag tag) {
        return getToolEffectivenessModifier(tag) * LEConfig.GENERAL.armorEffectivenessMultiplier.get();
    }


    public static double getToolEffectivenessModifier(CompoundTag tag) {
        if (!isLivingEnchantmentInitialized(tag)) {
            return 0.d;
        }
        return (getItemLevel(tag) * Personality.getPersonality(tag).levelEffectivenessMultiplier * LEConfig.GENERAL.globalEffectivenessMultiplier.get());
    }

    public static void addXp(Player player, ItemStack stack, CompoundTag tag, double xp) {
        if (!isLivingEnchantmentInitialized(tag)) {
            initLivingEnchantmentTag(tag, stack);
        }
        Personality personality = Personality.getPersonality(tag);

        double preXp = tag.getDouble(LEUtil.XP_KEY);
        int preLevel = xpToLevel(preXp);
        double postXp = preXp + xp * personality.xpGainMultiplier;
        int postLevel = xpToLevel(postXp);

        tag.putDouble(LEUtil.XP_KEY, postXp);

        if (preLevel != postLevel) {
            personality.talkOnLevelUp(player, stack, tag);
            Vec3 pos = player.position();
            int talentAwakeningLevel = LEConfig.GENERAL.talentAwakeningLevels.get().get(0);
            SoundEvent soundToPlay = SoundEvents.LEVEL_UP.get();
            if (Talent.getTalent(tag, true) != null && preLevel < talentAwakeningLevel && postLevel >= talentAwakeningLevel) {
                soundToPlay = SoundEvents.TALENT_UNLOCK.get();
            }
            player.level.playSound(null, pos.x, pos.y, pos.z, soundToPlay, SoundSource.MASTER, 1, (float)Math.random() * 0.2F - 0.8F);
        }
    }
    public static void initLivingEnchantmentTag(CompoundTag tag, ItemStack stack) {
        if (!tag.contains(UUID_KEY)) {
            tag.putString(UUID_KEY, UUID.randomUUID().toString());
        }
        if (!tag.contains(PERSONALITY_KEY)) {
            Personality.setPersonality(tag);
        }
        if (!tag.contains(TALENT_KEY)) {
            Talent.setTalent(tag, stack);
        }
    }

    public static boolean isLivingEnchantmentInitialized(CompoundTag tag) {
        return tag.contains(UUID_KEY);
    }

}
