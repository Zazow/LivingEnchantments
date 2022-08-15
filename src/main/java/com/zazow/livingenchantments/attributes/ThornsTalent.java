package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class ThornsTalent extends Talent {
    ThornsTalent(String name) {
        super(name, true, Target.HELMET, Target.CHESTPLATE, Target.LEGGINGS, Target.BOOTS, Target.SHIELD);
    }

    @Override
    public void onPlayerHurt(CompoundTag tag, ItemStack stack, LivingDamageEvent event) {
        Entity source = event.getSource().getEntity();
        if (source == null) {
            return;
        }

        source.hurt(DamageSource.playerAttack(
                (Player) event.getEntityLiving()),
                (float) (event.getAmount() * getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.thornsDamageMultipliers.get())));
    }

    @Override
    public void onShieldBlock(CompoundTag tag, ShieldBlockEvent event) {
        Entity source = event.getDamageSource().getEntity();
        if (source == null) {
            return;
        }

        source.hurt(DamageSource.playerAttack(
                (Player) event.getEntityLiving()),
                (float) (event.getBlockedDamage() * getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.thornsBlockedDamageMultipliers.get())));
    }

    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        if (containsAnyTarget(getItemTargets(stack), Target.HELMET, Target.CHESTPLATE, Target.LEGGINGS, Target.BOOTS)) {
            return new TranslatableComponent(this.descriptionTranslationKey + "_armor", getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.thornsDamageMultipliers.get()) * 100);
        }
        if (getItemTargets(stack).contains(Target.SHIELD)) {
            return new TranslatableComponent(this.descriptionTranslationKey + "_shield", getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.thornsBlockedDamageMultipliers.get()) * 100);
        }
        return new TranslatableComponent("");
    }
}
