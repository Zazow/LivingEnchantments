package com.zazow.livingenchantments.mixin;

import com.zazow.livingenchantments.attributes.Personality;
import com.zazow.livingenchantments.util.LEUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    private int getUnbreakingLevel(ItemStack stack) {
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag == null) {
            return 0;
        }

        int level = LEUtil.getItemLevel(tag);
        Personality personality = Personality.getPersonality(tag);
        if (personality == null) {
            return 0;
        }

        return (int) (level * personality.levelDurabilityMultiplier);
    }

    @Redirect(
            method = "hurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I"
            )
    )
    private int injected(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) + getUnbreakingLevel(stack);
    }
}
