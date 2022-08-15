package com.zazow.livingenchantments.attributes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.util.Random;

public class FlameBenderTalent extends Talent {
    FlameBenderTalent(String name) {
        super(name, true, Talent.Target.CHESTPLATE, Talent.Target.LEGGINGS, Talent.Target.BOOTS);
    }

    @Override
    public void onPlayerHurt(CompoundTag tag, ItemStack stack, LivingDamageEvent event) {
        if (event.getSource().isFire()) {
            int hurtAmount = (int) event.getAmount() * 5;
            stack.hurt(hurtAmount, new Random(), (ServerPlayer) event.getEntityLiving());
            event.setCanceled(true);
        }
    }
}
