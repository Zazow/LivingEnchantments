package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.LEMod;
import com.zazow.livingenchantments.config.LEConfig;
import com.zazow.livingenchantments.util.LEUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;
import java.util.*;

@Mod.EventBusSubscriber(modid = LEMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Personality extends net.minecraftforge.registries.ForgeRegistryEntry<Personality> {

    public enum Rarity {
        COMMON(0xFFFFFF, 10, false),
        UNCOMMON(0x1eff00, 20, false),
        RARE(0x0070dd, 40, false),
        EPIC(0xa335ee, 60, true),
        LEGENDARY(0xff8000, 100, true);

        public final int color;
        public final int maxLevel;
        public final boolean isBold;
        Rarity(int color, int maxLevel, boolean isBold) {
            this.color = color; this.maxLevel = maxLevel; this.isBold = isBold;
        }

        public Style getStyle() {
            return Style.EMPTY.withBold(isBold).withColor(color);
        }
    }
    // level = (xp / level * 255)
    public final String name;
    public final float levelEffectivenessMultiplier;
    public final float levelDurabilityMultiplier;
    public final double xpGainMultiplier;
    public final float talkingChance;
    public final String[] onUseMessages;
    public final String[] onKillMessages;
    public final String[] onDeathMessages;
    public final String[] onLevelUpMessages;
    public final String[] onHurtMessages;
    public final String[] onTwentyPercentMessages;
    public final String[] onFivePercentMessages;
    public final String[] onBreakMessages;



    // ItemType -> talent.
    // public final Map<>maxLevelTalent
    public final Rarity rarity;

    Personality(
            String name,
            float levelEffectivenessMultiplier,
            float levelDurabilityMultiplier,
            double xpGainMultiplier,
            float talkingChance,
            String[] onUseMessages,
            String[] onKillMessages,
            String[] onDeathMessages,
            String[] onLevelUpMessages,
            String[] onHurtMessages,
            String[] onTwentyPercentMessages,
            String[] onFivePercentMessages,
            String[] onBreakMessages,
            Rarity rarity) {
        setRegistryName(LEMod.MODID, "personality_" + name.toLowerCase());
        this.talkingChance = talkingChance;
        this.name = name;
        this.levelEffectivenessMultiplier = levelEffectivenessMultiplier;
        this.levelDurabilityMultiplier = levelDurabilityMultiplier;
        this.xpGainMultiplier = xpGainMultiplier;
        this.onUseMessages = onUseMessages;
        this.onKillMessages = onKillMessages;
        this.onDeathMessages = onDeathMessages;
        this.onLevelUpMessages = onLevelUpMessages;
        this.onHurtMessages = onHurtMessages;
        this.onTwentyPercentMessages = onTwentyPercentMessages;
        this.onFivePercentMessages = onFivePercentMessages;
        this.onBreakMessages = onBreakMessages;
        this.rarity = rarity;
    }

    protected  static Rarity getRandomRarity(Random rng) {
        double rand = rng.nextDouble();
        if (rand < LEConfig.GENERAL.legendaryOddsCutoff.get()) {
            return Rarity.LEGENDARY;
        }
        if (rand < LEConfig.GENERAL.epicOddsCutoff.get()) {
            return Rarity.EPIC;
        }
        if (rand < LEConfig.GENERAL.rareOddsCutoff.get()) {
            return Rarity.RARE;
        }
        if (rand < LEConfig.GENERAL.uncommonOddsCutoff.get()) {
            return Rarity.UNCOMMON;
        }
        return Rarity.COMMON;
    }

    public static void setPersonality(CompoundTag tag) {
        Random rng = new Random(UUID.fromString(tag.getString(LEUtil.UUID_KEY)).getMostSignificantBits());
        Rarity rarity = getRandomRarity(rng);
        List<ResourceLocation> possiblePersonalities = AttributeRegistry.personalitiesByRarity.get(rarity);
        int index = (int) (rng.nextDouble() * possiblePersonalities.size());
        tag.putString(LEUtil.PERSONALITY_KEY, possiblePersonalities.get(index).toString());
    }

    public static Personality getPersonality(CompoundTag tag) {
        return AttributeRegistry.personalityRegistrySupplier.get().getValue(new ResourceLocation(tag.getString(LEUtil.PERSONALITY_KEY)));
    }

    String getMessage(String[] messages, Player player, CompoundTag tag, ItemStack stack) {
        if (messages.length == 0) {
            return "";
        }
        return messages[(int) (Math.random() * messages.length)]
                .replace("$user", player.getDisplayName().getString())
                .replace("$durability", (stack.getDamageValue() / (stack.getMaxDamage() + 1)) * 100 + "%")
                .replace("$level", "" + LEUtil.getItemLevel(tag));
    }
    public void talk(
            Player player,
            ItemStack stack,
            CompoundTag tag,
            String[] messages,
            Style style,
            int talkWaitTime,
            boolean rollChanceToTalk) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        if (rollChanceToTalk && Math.random() > talkingChance) {
            return;
        }

        if (System.currentTimeMillis() - tag.getLong(LEUtil.LAST_TALK_KEY) < talkWaitTime) {
            return;
        }

        tag.putLong(LEUtil.LAST_TALK_KEY, System.currentTimeMillis());

        String message = getMessage(messages, player, tag, stack);
        ((ServerPlayer) player).sendMessage(new TranslatableComponent("personality.chat", stack.getDisplayName(), message).setStyle(style), ChatType.CHAT, player.getUUID());
    }
    public void talkOnUse(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onUseMessages, Style.EMPTY, LEConfig.GENERAL.talkWait.get(), true);
    }

    public void talkOnKill(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onKillMessages, Style.EMPTY, LEConfig.GENERAL.talkWait.get(), true);
    }

    public void talkOnDeath(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onDeathMessages, Style.EMPTY, 0, false);
    }

    public void talkOnLevelUp(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onLevelUpMessages, Style.EMPTY, 0, false);
    }

    public void talkOnHurt(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onHurtMessages, Style.EMPTY, LEConfig.GENERAL.talkWait.get(), true);
    }

    public void tryWarnDurability(Player player, ItemStack stack, CompoundTag tag) {
        if (stack.getMaxDamage() <= 0) {
            return;
        }
        double durability = stack.getDamageValue() / stack.getMaxDamage();
        if (durability >= 1 ) {
            talkOnBreak(player, stack, tag);
        } else if (durability > 0.95) {
            talkOnFivePercent(player, stack, tag);
        } else if (durability > 0.75) {
            talkOnTwentyPercent(player, stack, tag);
        }
    }

    public void talkOnTwentyPercent(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onTwentyPercentMessages, Style.EMPTY.withColor(ChatFormatting.YELLOW), LEConfig.GENERAL.durabilityWarningWait.get(), false);
    }
    public void talkOnFivePercent(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onFivePercentMessages, Style.EMPTY.withBold(true).withColor(ChatFormatting.DARK_RED), LEConfig.GENERAL.durabilityWarningWait.get(), false);
    }
    public void talkOnBreak(Player player, ItemStack stack, CompoundTag tag) {
        talk(player, stack, tag, onBreakMessages, Style.EMPTY.withColor(ChatFormatting.DARK_GRAY), 0, false);
    }

    protected static final DecimalFormat df = new DecimalFormat("0.0");

    public Component getPersonalityDescription() {
        return new TranslatableComponent(
                "personality.description",
                df.format(levelEffectivenessMultiplier),
                df.format(levelDurabilityMultiplier),
                df.format(xpGainMultiplier)
        );
    }

}
