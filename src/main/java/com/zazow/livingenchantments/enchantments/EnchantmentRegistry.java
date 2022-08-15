package com.zazow.livingenchantments.enchantments;

import com.zazow.livingenchantments.LEMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LEMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentRegistry {
    public static LivingEnchantment LIVING_ENCHANTMENT = new LivingEnchantment(
            Enchantment.Rarity.RARE,
            EnchantmentCategory.BREAKABLE,
            new EquipmentSlot[]{
                    EquipmentSlot.MAINHAND,
                    EquipmentSlot.HEAD,
                    EquipmentSlot.FEET,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.CHEST
            }
    );

    @SubscribeEvent
    public static void registerEnchantments(final RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(LIVING_ENCHANTMENT);
    }
}
