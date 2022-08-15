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

public class ProsperityTalent extends Talent {

    ProsperityTalent(String name) {
        super(name, true, Talent.Target.HELMET, Talent.Target.CHESTPLATE, Talent.Target.LEGGINGS, Talent.Target.BOOTS, Talent.Target.WEAPON, Talent.Target.DIGGER, Talent.Target.SHIELD);
    }
    static final UUID LUCK_ATTRIBUTE_UUID = UUID.randomUUID();
    public void onItemAttributeModifier(CompoundTag tag, ItemAttributeModifierEvent event) {
        event.addModifier(Attributes.LUCK, new AttributeModifier(
                LUCK_ATTRIBUTE_UUID,
                "livingenchantments",
                getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.luckTalentValues.get()),
                AttributeModifier.Operation.ADDITION)
        );
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        return new TranslatableComponent(this.descriptionTranslationKey, df.format(getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.luckTalentValues.get())));
    }
}
