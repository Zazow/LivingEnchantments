package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;

public class MeditationTalent extends Talent {
    MeditationTalent(String name) {
        super(
                name,
                true,
                Talent.Target.HELMET,
                Talent.Target.CHESTPLATE,
                Talent.Target.LEGGINGS,
                Talent.Target.BOOTS,
                Talent.Target.WEAPON,
                Talent.Target.DIGGER,
                Talent.Target.SHIELD
        );
    }

    @Override
    public void onPlayerWakeUp(CompoundTag tag, ItemStack stack, PlayerWakeUpEvent event) {
        stack.getBaseRepairCost();
        stack.setDamageValue(
                stack.getDamageValue() - (int) (stack.getMaxDamage() * getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.meditationFixPercentages.get()))
        );
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        return new TranslatableComponent(
                this.descriptionTranslationKey,
                df.format(getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.meditationFixPercentages.get()) * 100)
        );
    }
}
