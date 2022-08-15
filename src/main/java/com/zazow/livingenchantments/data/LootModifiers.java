package com.zazow.livingenchantments.data;

import com.zazow.livingenchantments.LEMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LootModifiers extends GlobalLootModifierProvider {

    protected List<Locale.Builder> lootBuilders = new ArrayList<>();

    public LootModifiers(DataGenerator gen) {
        super(gen, LEMod.MODID);
    }

    private void addLoot() {

    }

    @Override
    protected void start() {

    }
}
