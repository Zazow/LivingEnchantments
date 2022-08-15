package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class LifeLeechTalent extends Talent {
    LifeLeechTalent(String name) {
        super(name, true, new Target[]{Target.WEAPON, Target.SHIELD});
    }

    @Override
    public void onPlayerDealsDamage(CompoundTag tag, LivingDamageEvent event) {
        ServerPlayer player = (ServerPlayer) event.getSource().getEntity();
        if (player == null) {
            return;
        }

        player.heal((float) (event.getAmount() * getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.lifeLeechDamageFractions.get())));
    }

    @Override
    public void onShieldBlock(CompoundTag tag, ShieldBlockEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (player == null) {
            return;
        }

        player.heal((float) (event.getBlockedDamage() * getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.lifeLeechBlockFractions.get())));
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        if (getItemTargets(stack).contains(Target.WEAPON)) {
            return new TranslatableComponent(this.descriptionTranslationKey + "_weapon", getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.lifeLeechDamageFractions.get()) * 100);
        }
        if (getItemTargets(stack).contains(Target.SHIELD)) {
            return new TranslatableComponent(this.descriptionTranslationKey + "_shield", getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.lifeLeechBlockFractions.get()) * 100);
        }
        return new TranslatableComponent("");
    }
}
