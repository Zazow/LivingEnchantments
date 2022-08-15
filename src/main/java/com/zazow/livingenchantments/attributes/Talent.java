package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.LEMod;
import com.zazow.livingenchantments.config.LEConfig;
import com.zazow.livingenchantments.util.LEUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = LEMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Talent extends net.minecraftforge.registries.ForgeRegistryEntry<Talent> {
    enum Target {
        NONE,
        DIGGER,
        WEAPON,
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        SHIELD
}

    public final String name;
    public final String descriptionTranslationKey;
    public final Target[] targets;
    public final boolean shouldInterpLevel;

    Talent(String name, boolean shouldInterpLevel, Target... targets) {
        setRegistryName(LEMod.MODID, "talent_" + name.toLowerCase());
        this.name = name;
        this.descriptionTranslationKey = getRegistryName().toString() + "_description";
        this.targets = targets;
        this.shouldInterpLevel = shouldInterpLevel;

    }

    // Only gets called for items in main hand.
    public void onBlockBreak(Player player, CompoundTag tag, BlockState state, BlockPos pos) {

    }

    // Only gets called for items in main hand.
    public float onBreakSpeedCalculation(Player player, CompoundTag tag, PlayerEvent.BreakSpeed event) { return 1.f; }

    public void onPlayerDealsDamage(CompoundTag tag, LivingDamageEvent event) {}

    // Only gets called for worn armor pieces.
    public void onPlayerHurt(CompoundTag tag, ItemStack stack, LivingDamageEvent event) {}

    public void onShieldBlock(CompoundTag tag, ShieldBlockEvent event) {}

    public void onItemAttributeModifier(CompoundTag tag, ItemAttributeModifierEvent event) {}

    public void onPlayerWakeUp(CompoundTag tag, ItemStack stack, PlayerWakeUpEvent event) {}

    public static List<Target> getItemTargets(ItemStack stack) {
        List<Target> targets = new ArrayList<>();

        Item item = stack.getItem();

        if (item instanceof DiggerItem ||
            item instanceof ShearsItem) {
            targets.add(Target.DIGGER);
        }

        if (item instanceof AxeItem ||
            item instanceof SwordItem ||
            item instanceof BowItem ||
            item instanceof CrossbowItem
        ) {
            targets.add(Target.WEAPON);
        }

        if (item instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem) item;
            switch (armor.getSlot()) {
                case HEAD:
                    targets.add(Target.HELMET);
                    break;
                case CHEST:
                    targets.add(Target.CHESTPLATE);
                case LEGS:
                    targets.add(Target.LEGGINGS);
                case FEET:
                    targets.add(Target.BOOTS);
            }
        }

        if (item instanceof ShieldItem) {
            targets.add(Target.SHIELD);
        }

        return targets;
    }
    public static void setTalent(CompoundTag tag, ItemStack stack) {
        List<Target> targets = getItemTargets(stack);

        boolean shouldApplyTalent;
        if (Personality.getPersonality(tag).rarity == Personality.Rarity.LEGENDARY) {
            shouldApplyTalent = Math.random() < LEConfig.GENERAL.legendaryTalentChanceOverride.get();
        } else {
            shouldApplyTalent = Math.random() < LEConfig.GENERAL.talentChance.get();
        }

        if (!shouldApplyTalent) {
            return;
        }

        Target target = targets.get((int)(Math.random() * targets.size()));
        List<ResourceLocation> targetTalents = AttributeRegistry.talentByTarget.get(target);
        ResourceLocation talent = targetTalents.get((int) (Math.random() * targetTalents.size()));
        tag.putString(LEUtil.TALENT_KEY, talent.toString());
    }

    public static Talent getTalent(CompoundTag tag, boolean getEvenIfNotAwakened) {
        if (!tag.contains(LEUtil.TALENT_KEY) || (!Talent.isTalentAwakened(tag) && !getEvenIfNotAwakened)) {
            return null;
        }
        return AttributeRegistry.talentRegistrySupplier.get().getValue(new ResourceLocation(tag.getString(LEUtil.TALENT_KEY)));
    }

    public static boolean isTalentAwakened(CompoundTag tag) {
        if (LEConfig.GENERAL.talentAwakeningLevels.get().size() == 0) {
            return false;
        }
        return LEUtil.getItemLevel(tag) >= LEConfig.GENERAL.talentAwakeningLevels.get().get(0);
    }

    public double getConfigValueBasedOnLevel(CompoundTag tag, List<? extends Number> configList) {
        int level = LEUtil.getItemLevel(tag);
        List<Integer> awakeningLevels = LEConfig.GENERAL.talentAwakeningLevels.get();
        for (int i = Math.min(configList.size(), awakeningLevels.size()) - 1; i >= 0 ; --i) {
            int awakingLevel = LEConfig.GENERAL.talentAwakeningLevels.get().get(i);
            if (level >= awakingLevel) {
                if (shouldInterpLevel && i < configList.size() - 1 && i < awakeningLevels.size() - 1) {
                    double u = (level - awakeningLevels.get(i)) / awakeningLevels.get(i + 1);
                    return configList.get(i).doubleValue() * (1-u) + configList.get(i + 1).doubleValue() * u;
                }
                return configList.get(i).doubleValue();
            }
        }
        return 0;
    }

    public static boolean containsAnyTarget(List<Target> targets, Target... inTargets) {
        for (Target target : inTargets) {
            if (targets.contains(target)) {
                return true;
            }
        }
        return false;
    }

    protected static final DecimalFormat df = new DecimalFormat("0.0");
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        return new TranslatableComponent(this.descriptionTranslationKey);
    }
}
