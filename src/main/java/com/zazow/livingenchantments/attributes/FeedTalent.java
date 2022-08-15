package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class FeedTalent extends Talent {
    FeedTalent(String name) {
        super(name, true, new Target[]{Target.WEAPON});
    }

    @Override
    public void onPlayerDealsDamage(CompoundTag tag, LivingDamageEvent event) {
        ServerPlayer player = (ServerPlayer) event.getSource().getEntity();
        if (player == null) {
            return;
        }

        if (Math.random() >= getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.feedOdds.get())) {
            return;
        }

        player.getFoodData().eat(
                (int) (event.getAmount() * getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.feedDamageFractions.get())),
                (float) getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.saturationDamageFractions.get())
        );
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        double odds = getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.feedOdds.get());
        double foodFraction = getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.feedDamageFractions.get());
        return new TranslatableComponent(this.descriptionTranslationKey, df.format(odds * 100), df.format(foodFraction * 100));
    }
}
