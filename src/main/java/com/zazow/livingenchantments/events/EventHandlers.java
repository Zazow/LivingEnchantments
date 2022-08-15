package com.zazow.livingenchantments.events;

import com.zazow.livingenchantments.LEMod;
import com.zazow.livingenchantments.attributes.AttributeRegistry;
import com.zazow.livingenchantments.attributes.Personality;
import com.zazow.livingenchantments.attributes.Talent;
import com.zazow.livingenchantments.config.LEConfig;
import com.zazow.livingenchantments.util.LEUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraft.nbt.CompoundTag;

import java.text.DecimalFormat;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = LEMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlers {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide()) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack heldItem = player.getMainHandItem();

        CompoundTag tag = LEUtil.getLivingEnchantmentTag(heldItem);
        if (tag == null) {
            return;
        }

        if (!(heldItem.getItem() instanceof DiggerItem) ||
            !LEUtil.isToolEffective((DiggerItem) heldItem.getItem(), event.getState())) {
            return;
        }

        tag.putInt(LEUtil.USAGE_COUNT_KEY, tag.getInt(LEUtil.USAGE_COUNT_KEY) + 1);
        double xp = LEConfig.GENERAL.blockBreakBaseXpReward.get() + event.getState().getDestroySpeed(event.getWorld(), event.getPos()) * LEConfig.GENERAL.blockHardnessXpMultiplier.get();
        LEUtil.addXp(player, heldItem, tag, xp);

        Personality personality = Personality.getPersonality(tag);
        if (personality != null) {
            personality.tryWarnDurability(player, heldItem, tag);
            personality.talkOnUse(player, heldItem, tag);
        }

        Talent talent = Talent.getTalent(tag, false);
        if (talent != null) {
            talent.onBlockBreak(player, tag, event.getState(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void breakSpeedEvent(PlayerEvent.BreakSpeed event) {
        ItemStack item = event.getPlayer().getMainHandItem();
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(item);
        if (tag == null) {
            return;
        }

        if (!(item.getItem() instanceof DiggerItem) ||
            !LEUtil.isToolEffective((DiggerItem) item.getItem(), event.getState())) {
            return;
        }

        double multiplier = LEUtil.getBreakSpeedMultiplier(tag);

        Talent talent = Talent.getTalent(tag, false);
        if (talent != null) {
            multiplier *= talent.onBreakSpeedCalculation(event.getPlayer(), tag, event);
        }

        event.setNewSpeed((float) (event.getNewSpeed() * multiplier));
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingDamageEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            onPlayerTakesDamage(event);
        }

        if (event.getSource().getEntity() instanceof Player) {
            onPlayerDealsDamage(event);
        }
    }

    public static void onPlayerDealsDamage(LivingDamageEvent event) {
        Player player = (Player) event.getSource().getEntity();

        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return;
        }

        if (!stack.getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE)) {
            // Attack damage attribute did not modify this damage event, so we'll manually do it here.
            event.setAmount(event.getAmount() * ((float) LEUtil.getDamageMultiplier(tag) + 1));
        }

        tag.putInt(LEUtil.USAGE_COUNT_KEY, tag.getInt(LEUtil.USAGE_COUNT_KEY) + 1);

        String damagedEntityRegKey = event.getEntityLiving().getType().getRegistryName().toString();
        double multiplier = 1.f;
        if (LEConfig.GENERAL.getEntityDamageXpModifier().containsKey(damagedEntityRegKey)) {
            multiplier = LEConfig.GENERAL.getEntityDamageXpModifier().get(damagedEntityRegKey);
        }
        double xp = LEConfig.GENERAL.damageDealtBaseXpReward.get() + event.getAmount() * LEConfig.GENERAL.damageDealtXpMultiplier.get();
        LEUtil.addXp(player, stack, tag, xp * multiplier);

        Personality personality = Personality.getPersonality(tag);
        if (personality != null) {
            personality.tryWarnDurability(player, stack, tag);
            personality.talkOnHurt(player, stack, tag);
            personality.talkOnUse(player, stack, tag);
        }

        Talent talent = Talent.getTalent(tag, false);
        if (talent == null) {
            return;
        }

        talent.onPlayerDealsDamage(tag, event);
    }

    public static void onPlayerTakesDamage(LivingDamageEvent event) {
        Player player = (Player) event.getEntityLiving();

        for (ItemStack stack : player.getArmorSlots()) {
            CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
            if (tag == null) {
                continue;
            }

            tag.putInt(LEUtil.USAGE_COUNT_KEY, tag.getInt(LEUtil.USAGE_COUNT_KEY) + 1);
            double xp = LEConfig.GENERAL.damageTakenBaseXpReward.get() + event.getAmount() * LEConfig.GENERAL.damageTakenXpMultiplier.get();
            LEUtil.addXp(player, stack, tag, xp);

            Personality personality = Personality.getPersonality(tag);
            if (personality != null) {
                personality.talkOnUse(player, stack, tag);
                personality.tryWarnDurability(player, stack, tag);
            }

            Talent talent = Talent.getTalent(tag, false);
            if (talent == null) {
                continue;
            }

            talent.onPlayerHurt(tag, stack, event);
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntityLiving();

        ItemStack stack = player.getUseItem();
        if (stack == null) {
            return;
        }
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);

        if (tag == null) {
            return;
        }

        tag.putInt(LEUtil.USAGE_COUNT_KEY, tag.getInt(LEUtil.USAGE_COUNT_KEY) + 1);
        double xp = LEConfig.GENERAL.blockingBaseXpReward.get() + event.getBlockedDamage() * LEConfig.GENERAL.damageBlockedXpMultiplier.get();
        LEUtil.addXp(player, stack, tag, xp);

        Personality personality = Personality.getPersonality(tag);
        if (personality != null) {

            personality.tryWarnDurability(player, stack, tag);
        }

        Talent talent = Talent.getTalent(tag, false);
        if (talent == null) {
            return;
        }

        talent.onShieldBlock(tag, event);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            onPlayerDeath(event);
        }

        if (event.getSource().getEntity() instanceof Player) {
            onEntityDeathByPlayer(event);
        }
    }
    public static void onPlayerDeath(LivingDeathEvent event) {
        Player player = (Player) event.getEntityLiving();

        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return;
        }

        Personality personality = Personality.getPersonality(tag);
        if (personality != null) {
            personality.talkOnDeath(player, stack, tag);
        }
    }

    public static void onEntityDeathByPlayer(LivingDeathEvent event) {
        Player player = (Player) event.getSource().getEntity();

        ItemStack stack = player.getMainHandItem();
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return;
        }

        double xp = LEConfig.GENERAL.killingBaseXpReward.get() + event.getEntityLiving().getMaxHealth() * LEConfig.GENERAL.killedEntityHealthXpMultiplier.get();
        LEUtil.addXp(player, stack, tag, xp);

        Personality personality = Personality.getPersonality(tag);
        if (personality != null) {
            personality.talkOnKill(player, stack, tag);
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {

    }

    @SubscribeEvent
    public static void onHoeUsed(UseHoeEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack == null) {
            return;
        }

        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return;
        }

        tag.putInt(LEUtil.USAGE_COUNT_KEY, tag.getInt(LEUtil.USAGE_COUNT_KEY) + 1);
        double xp = LEConfig.GENERAL.hoeBaseXpReward.get();
        LEUtil.addXp(player, stack, tag, xp);

        Personality personality = Personality.getPersonality(tag);
        if (personality != null) {
            personality.tryWarnDurability(player, stack, tag);
            personality.talkOnUse(player, stack, tag);
        }
    }

    static final UUID ARMOUR_ATTRIBUTE_UUID = UUID.randomUUID();
    static final UUID ATTACK_DAMAGE_ATTRIBUTE_UUID = UUID.randomUUID();
    static final UUID ATTACK_SPEED_ATTRIBUTE_UUID = UUID.randomUUID();


    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return;
        }
        EquipmentSlot slot = event.getSlotType();

        if (stack.getItem() instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem) stack.getItem();
            if (armor.getSlot() != slot) {
                return;
            }

            event.addModifier(Attributes.ARMOR, new AttributeModifier(ARMOUR_ATTRIBUTE_UUID, "livingenchantments", LEUtil.getArmorMultiplier(tag), AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else if (event.getModifiers().containsKey(Attributes.ATTACK_DAMAGE) && slot == EquipmentSlot.MAINHAND) {

                event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_ATTRIBUTE_UUID, "livingenchantments", LEUtil.getAttackSpeedMultiplier(tag), AttributeModifier.Operation.MULTIPLY_TOTAL));
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_ATTRIBUTE_UUID, "livingenchantments", LEUtil.getDamageMultiplier(tag), AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

        Talent talent = Talent.getTalent(tag, false);
        if (talent == null) {
            return;
        }

        talent.onItemAttributeModifier(tag, event);
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getPlayer();
        for (ItemStack stack : player.getInventory().items) {
            CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
            if (tag == null) {
                continue;
            }
            Talent talent = Talent.getTalent(tag, false);
            if (talent == null) {
                continue;
            }
            talent.onPlayerWakeUp(tag, stack, event);
        }
    }

    @SubscribeEvent
    public static void onEnderManAnger(EnderManAngerEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        ItemStack helmet = player.getInventory().getArmor(3);
        if (helmet == null) {
            return;
        }

        CompoundTag tag = LEUtil.getLivingEnchantmentTag(helmet);
        if (tag == null) {
            return;
        }

        Talent talent = Talent.getTalent(tag, false);
        if (talent != AttributeRegistry.ENDER_PR_TALENT) {
            return;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityMobGriefing(EntityMobGriefingEvent event) {
        for (Player player : event.getEntity().getLevel().players()) {
            if (player.position().distanceTo(event.getEntity().position()) <= LEConfig.GENERAL.protectorRange.get()) {
                for (ItemStack stack : player.getArmorSlots()) {
                    CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
                    if (tag == null) {
                        continue;
                    }

                    Talent talent = Talent.getTalent(tag, false);
                    if (talent != AttributeRegistry.PROTECTOR_TALENT) {
                        continue;
                    }
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilRepair(AnvilRepairEvent event) {
        CompoundTag inTag = LEUtil.getLivingEnchantmentTag(event.getItemInput());
        CompoundTag outTag = LEUtil.getLivingEnchantmentTag(event.getItemResult());

        if (inTag == null || outTag == null) {
            return;
        }

        outTag.putString(LEUtil.UUID_KEY, inTag.getString(LEUtil.UUID_KEY));
        outTag.putInt(LEUtil.USAGE_COUNT_KEY, inTag.getInt(LEUtil.USAGE_COUNT_KEY));
        outTag.putDouble(LEUtil.XP_KEY, inTag.getDouble(LEUtil.XP_KEY));
        outTag.putString(LEUtil.TALENT_KEY, inTag.getString(LEUtil.TALENT_KEY));
        outTag.putString(LEUtil.PERSONALITY_KEY, inTag.getString(LEUtil.PERSONALITY_KEY));
        outTag.putLong(LEUtil.LAST_TALK_KEY, inTag.getLong(LEUtil.LAST_TALK_KEY));
    }

    private static final DecimalFormat df = new DecimalFormat("0.0");
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        LocalPlayer player = (LocalPlayer) event.getPlayer();
        if (player == null) {
            return;
        }
        ItemStack stack = event.getItemStack();
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return;
        }

        if (!LEUtil.isLivingEnchantmentInitialized(tag)) {
            event.getToolTip().add(1, new TranslatableComponent("tooltip.item_not_initialized" ));
            return;
        }

        Personality personality = Personality.getPersonality(tag);

        int personalityIndex = 1;
        event.getToolTip().add(personalityIndex, new TranslatableComponent("tooltip.personality", new TranslatableComponent(tag.getString(LEUtil.PERSONALITY_KEY)).setStyle(personality.rarity.getStyle())));
        if (Screen.hasShiftDown()) {
            event.getToolTip().add(++personalityIndex, personality.getPersonalityDescription());
        }
        event.getToolTip().add(personalityIndex + 1, new TranslatableComponent("tooltip.level", "" + LEUtil.getItemLevel(tag)).setStyle(Style.EMPTY.withBold(true)));
        event.getToolTip().add(personalityIndex + 2, new TranslatableComponent("tooltip.xp", df.format(LEUtil.getItemXp(tag)), df.format(LEUtil.getItemXpToNextLevel(tag))));


        Talent talent = Talent.getTalent(tag, true);
        if (talent != null) {
            if (talent.isTalentAwakened(tag)) {
                event.getToolTip().add(personalityIndex + 1, new TranslatableComponent("tooltip.talent", new TranslatableComponent(talent.getRegistryName().toString())).setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW).withBold(true)));
                if (Screen.hasShiftDown()) {
                    event.getToolTip().add(personalityIndex + 2, talent.getTalentDescription(tag, stack));
                }
            } else {
                event.getToolTip().add(personalityIndex + 1, new TranslatableComponent("tooltip.talent_not_awakened").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW).withBold(true)));
            }
        }
    }

}
