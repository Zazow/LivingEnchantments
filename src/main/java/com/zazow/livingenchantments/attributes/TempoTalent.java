package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public class TempoTalent extends Talent {
    TempoTalent(String name) {
        super(name, true, Target.BOOTS, Target.LEGGINGS);
    }

    static final UUID MOVEMENT_SPEED_ATTRIBUTE_UUID = UUID.randomUUID();
    public void onItemAttributeModifier(CompoundTag tag, ItemAttributeModifierEvent event) {
        event.addModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                MOVEMENT_SPEED_ATTRIBUTE_UUID,
                "livingenchantments",
                getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.tempoMovementSpeedMultipliers.get()),
                AttributeModifier.Operation.MULTIPLY_TOTAL)
        );
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        return new TranslatableComponent(
                this.descriptionTranslationKey,
                df.format(
                        (getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.tempoMovementSpeedMultipliers.get()) + 1) * 100));
    }
}
