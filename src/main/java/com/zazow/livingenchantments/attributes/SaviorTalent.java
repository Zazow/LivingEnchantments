package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SaviorTalent extends Talent {

    public final static String[] onSaveMessages = new String[]{

    };
    SaviorTalent(String name) {
        super(name, true, Talent.Target.CHESTPLATE, Talent.Target.LEGGINGS);
    }

    public boolean checkTotemDeathProtection(CompoundTag tag, ItemStack stack, Player player) {
        boolean shouldSave = Math.random() < getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.saviorChanceOdds.get());
        if (shouldSave) {
            player.sendMessage(
                    new TranslatableComponent("livingenchantments:talent_savior_save_message", stack.getDisplayName()),
                    player.getUUID());
        }
        return shouldSave;
    }
}
