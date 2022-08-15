package com.zazow.livingenchantments.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class LootTables extends LootTableProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = new ArrayList<>();

    public LootTables(DataGenerator p_124437_) {
        super(p_124437_);
    }

//    @Override
//    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
//        tables.clear();
//        addDrinkingHatsLootTable();
//        addArtifactsLootTable();
//        addChestLootTables();
//
//        for (LootModifiers.Builder lootBuilder : lootModifiers.lootBuilders) {
//            addLootTable("inject/" + lootBuilder.getName(), lootBuilder.createLootTable(), lootBuilder.getParameterSet());
//        }
//
//        addLootTable(MimicEntity.LOOT_TABLE.getPath(), new LootTable.Builder().withPool(new LootPool.Builder().add(artifact(1))));
//
//
//        return tables;
//    }

}
