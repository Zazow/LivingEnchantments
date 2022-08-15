package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

public class VitalityTalent extends Talent {
    static final UUID MAX_HEALTH_ATTRIBUTE_UUID = UUID.randomUUID();

    VitalityTalent(String name) {
        super(name, true, Target.SHIELD);
    }

    @Override
    public void onItemAttributeModifier(CompoundTag tag, ItemAttributeModifierEvent event) {
        if (event.getSlotType() != EquipmentSlot.OFFHAND) {
            return;
        }
        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(
                MAX_HEALTH_ATTRIBUTE_UUID,
                "livingenchantments",
                getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.vitalityTalentValues.get()),
                AttributeModifier.Operation.ADDITION
        ));
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        return new TranslatableComponent(
                this.descriptionTranslationKey,
                df.format(getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.vitalityTalentValues.get()))
        );
    }
}
