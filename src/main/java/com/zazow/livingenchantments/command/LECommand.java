package com.zazow.livingenchantments.command;

import com.zazow.livingenchantments.attributes.AttributeRegistry;
import com.zazow.livingenchantments.attributes.Personality;
import com.zazow.livingenchantments.attributes.Talent;
import com.zazow.livingenchantments.enchantments.EnchantmentRegistry;
import com.zazow.livingenchantments.util.LEUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LECommand {
    public static List<String> talents;
    public static List<String> personalities;

    public static void init() {
        talents = new ArrayList<>();
        for (Talent talent: AttributeRegistry.talentRegistrySupplier.get()) {
            talents.add(talent.getRegistryName().toString());
        }

        personalities = new ArrayList<>();
        for (Personality personality: AttributeRegistry.personalityRegistrySupplier.get()) {
            personalities.add(personality.getRegistryName().toString());
        }
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("livingenchantments")
                .then(Commands.literal("set")
                        .requires(player -> player.hasPermission(2))
                        .then(Commands.literal("talent")
                                .then(Commands.argument("Talent", StringArgumentType.greedyString())
                                        .suggests(((context, builder) -> SharedSuggestionProvider.suggest(talents, builder)))
                                        .executes(LECommand::executeSetTalentCommand)
                                )
                        )
                        .then(Commands.literal("xp")
                                .then(Commands.argument("Xp", DoubleArgumentType.doubleArg())
                                        .executes(LECommand::executeSetXpCommand)
                                )
                        )
                        .then(Commands.literal("personality")
                                .then(Commands.argument("Personality", StringArgumentType.greedyString())
                                        .suggests(((context, builder) -> SharedSuggestionProvider.suggest(personalities, builder)))
                                        .executes(LECommand::executeSetPersonalityCommand)
                                )
                        )
                )
        );
    }

    public static CompoundTag getTagOrEnchant(ItemStack stack) {
        CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
        if (tag != null) {
            return tag;
        }

        stack.enchant(EnchantmentRegistry.LIVING_ENCHANTMENT, 1);
        tag = LEUtil.getLivingEnchantmentTag(stack);
        LEUtil.initLivingEnchantmentTag(tag, stack);
        return tag;
    }
    public static int executeSetTalentCommand(CommandContext<CommandSourceStack> context) throws CommandRuntimeException, CommandSyntaxException {
        Player sender = context.getSource().getPlayerOrException();
        String talent = context.getArgument("Talent", String.class);

        ItemStack stack = sender.getMainHandItem();
        if (stack == null) {
            return 0;
        }

        CompoundTag tag = getTagOrEnchant(stack);
        if (tag == null) {
            return 0;
        }

        tag.putString(LEUtil.TALENT_KEY, talent);
        return 1;
    }

    public static int executeSetPersonalityCommand(CommandContext<CommandSourceStack> context) throws CommandRuntimeException, CommandSyntaxException {
        Player sender = context.getSource().getPlayerOrException();
        String personality = context.getArgument("Personality", String.class);

        ItemStack stack = sender.getMainHandItem();
        if (stack == null) {
            return 0;
        }

        CompoundTag tag = getTagOrEnchant(stack);
        if (tag == null) {
            return 0;
        }

        tag.putString(LEUtil.PERSONALITY_KEY, personality);
        return 1;
    }

    public static int executeSetXpCommand(CommandContext<CommandSourceStack> context) throws CommandRuntimeException, CommandSyntaxException {
        Player sender = context.getSource().getPlayerOrException();
        Double xp = context.getArgument("Xp", Double.class);

        ItemStack stack = sender.getMainHandItem();
        if (stack == null) {
            return 0;
        }

        CompoundTag tag = getTagOrEnchant(stack);
        if (tag == null) {
            return 0;
        }

        tag.putDouble(LEUtil.XP_KEY, xp);
        return 1;
    }
}
