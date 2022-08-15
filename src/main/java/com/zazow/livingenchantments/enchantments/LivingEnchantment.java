package com.zazow.livingenchantments.enchantments;

import com.zazow.livingenchantments.LEMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LivingEnchantment extends Enchantment {

    protected LivingEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
        this.setRegistryName(LEMod.MODID, "living_enchantment");
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem ||
               stack.getItem() instanceof HoeItem ||
               stack.getItem() instanceof SwordItem ||
               stack.getItem() instanceof AxeItem ||
               stack.getItem() instanceof PickaxeItem ||
               stack.getItem() instanceof ShovelItem ||
               stack.getItem() instanceof BowItem;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return this != other;
    }

    public Component getFullname(int p_44701_) {
        MutableComponent mutablecomponent = new TranslatableComponent(this.getDescriptionId());
        mutablecomponent.withStyle(ChatFormatting.GREEN);
        return mutablecomponent;
    }

    public void doPostAttack(LivingEntity p_44686_, Entity p_44687_, int p_44688_) {
    }

    public void doPostHurt(LivingEntity p_44692_, Entity p_44693_, int p_44694_) {
    }

    public boolean isTreasureOnly() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }

    public boolean isTradeable() {
        return true;
    }

    public boolean isDiscoverable() {
        return true;
    }
}
