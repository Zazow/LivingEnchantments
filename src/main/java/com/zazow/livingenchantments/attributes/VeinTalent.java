package com.zazow.livingenchantments.attributes;

import com.zazow.livingenchantments.config.LEConfig;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import com.zazow.livingenchantments.events.ClientEventHandlers;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.*;

public class VeinTalent extends Talent {


    abstract class BlockTraverser implements Iterator<Tuple<BlockPos, Integer>> {
        final Level level;
        final BlockPos pos;
        final Player player;
        final Direction direction;
        BlockTraverser(Level level, Player player, BlockPos pos) {
            this.level = level;
            this.player = player;
            this.pos = pos;
            this.direction = calcRayTrace(level, player).getDirection();
        }

        public static BlockHitResult calcRayTrace(Level worldIn, Player player) {
            float pitch = player.getXRot();
            float yaw = player.getYRot();
            Vec3 from = player.getEyePosition(1.0F);
            double f2 = Math.cos(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
            double f3 = Math.sin(-yaw * ((float)Math.PI / 180F) - (float)Math.PI);
            double f4 = -Math.cos(-pitch * ((float)Math.PI / 180F));
            double f5 = Math.sin(-pitch * ((float)Math.PI / 180F));
            double f6 = f3 * f4;
            double f7 = f2 * f4;
            double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();;
            Vec3 to = from.add(f6 * d0, f5 * d0, f7 * d0);
            return worldIn.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, player));
        }

        void onPosAccepted(BlockPos inPos, int distance) {}
    }

    class GridTraverser extends BlockTraverser {

        int currentIndex;
        final int gridSize;
        GridTraverser(Level level, Player player, BlockPos pos, int gridSize) {
            super(level, player, pos);
            this.gridSize = gridSize;
            this.currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < (gridSize * gridSize);
        }

        @Override
        public Tuple<BlockPos, Integer> next() {
            int i = (currentIndex % gridSize) - (gridSize / 2);
            int j = (currentIndex / gridSize) - (gridSize / 2);
            Vec3i offset = switch (direction.getAxis()) {
                case X -> new Vec3i(0, i, j);
                case Y -> new Vec3i(i, 0, j);
                case Z -> new Vec3i(i, j, 0);
            };
            ++currentIndex;
            return new Tuple<>(pos.offset(offset), Math.max(Math.abs(i), Math.abs(j)));
        }
    }

    class VeinTraverser extends BlockTraverser {
        final int maxDistance;
        final Set<BlockPos> visited;
        final LinkedList<Tuple<BlockPos, Integer>> candidates;

        VeinTraverser(Level level, Player player, BlockPos pos, int maxDistance) {
            super(level, player, pos);
            this.maxDistance = maxDistance;
            this.visited = Sets.newHashSet(pos);
            this.candidates = new LinkedList<>();
            addValidNeighbors(pos, 1);
        }

        public void addCandidateIfNotVisited(Tuple<BlockPos, Integer> candidate) {
            if (visited.contains(candidate.getA())) {
                return;
            }
            candidates.add(candidate);
        }

        public void addValidNeighbors(BlockPos source, int distance) {
            if (distance >= maxDistance) {
                return;
            }

            BlockPos up = source.above();
            addCandidateIfNotVisited(new Tuple<>(up, distance + 1));
            BlockPos down = source.below();
            addCandidateIfNotVisited(new Tuple<>(down, distance + 1));
            BlockPos[] blockPositions = new BlockPos[]{source, up, down};

            for (BlockPos blockPos : blockPositions) {
                addCandidateIfNotVisited(new Tuple<>(blockPos.west(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.east(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.north(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.south(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.north().west(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.north().east(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.south().west(), distance + 1));
                addCandidateIfNotVisited(new Tuple<>(blockPos.south().east(), distance + 1));
            }
        }

        void onPosAccepted(BlockPos inPos, int distance) {
            addValidNeighbors(inPos, distance);
        }

        @Override
        public boolean hasNext() {
            return !candidates.isEmpty();
        }

        @Override
        public Tuple<BlockPos, Integer> next() {
            return candidates.poll();
        }
    }


    public static final Map<UUID, Long> playersWantingToVeinMine = new HashMap<>();
    private static final Set<UUID> playersVeinMining = new HashSet<>();

    public final boolean isGrid;

    public VeinTalent(String name, boolean isGrid) {
        super(name, !isGrid, Target.DIGGER);
        this.isGrid = isGrid;
    }

    private static boolean removeBlock(Player player, BlockPos pos, boolean canHarvest) {
        Level world = player.getCommandSenderWorld();
        BlockState state = world.getBlockState(pos);
        boolean removed =
                state.onDestroyedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

        if (removed) {
            state.getBlock().destroy(world, pos, state);

            if (!world.getBlockState(pos).isAir()) {
                world.removeBlock(pos, false);
            }
        }
        return removed;
    }

    public static boolean harvest(ServerPlayer player, BlockPos pos, BlockPos originPos) {
        ServerLevel world = player.getLevel();
        BlockState state = world.getBlockState(pos);
        GameType gameType = player.gameMode.getGameModeForPlayer();

        if (state.getBlock() == Blocks.BEDROCK || !ForgeHooks.isCorrectToolForDrops(state, player)) {
            return false;
        }

        int exp = ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);

        if (exp == -1) {
            return false;
        }

        BlockEntity tileEntity = world.getBlockEntity(pos);
        Block block = state.getBlock();

        if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
            return true;
        }

        if (player.blockActionRestricted(world, pos, gameType)) {
            return false;
        }

        if (gameType.isCreative()) {
            removeBlock(player, pos, false);
            return true;
        }

        ItemStack stack = player.getMainHandItem();
        stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

        boolean removed = removeBlock(player, pos, state.canHarvestBlock(world, pos, player));

        if (removed) {
            player.awardStat(Stats.BLOCK_MINED.get(block));
            player.causeFoodExhaustion((float) (0.005f));
            Block.getDrops(state, world, pos, tileEntity, player, stack).forEach(
                    (stackToSpawn) -> Block.popResource(world, originPos, stackToSpawn));
            state.spawnAfterBreak(world, pos, stack);
            if (exp > 0) {
                state.getBlock().popExperience(world, originPos, exp);
            }
        }

        return true;
    }

    protected BlockTraverser getTraverser(CompoundTag tag, Level level, Player player, BlockPos pos) {
        if (isGrid) {
            int gridSize = (int)getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.gridMiningSizes.get());
            return new GridTraverser(level, player, pos, gridSize);
        }

        return new VeinTraverser(level, player, pos, 10);
    }

    protected int getMaxBlocks(CompoundTag tag) {
        if (isGrid) {
            return 999;
        }
        double numBlocks = getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.veinMinerMaxBlocks.get());
        return (int) numBlocks;
    }
    @Override
    public void onBlockBreak(Player player, CompoundTag tag, BlockState state, BlockPos pos) {
        super.onBlockBreak(player, tag, state, pos);
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        if (!playersWantingToVeinMine.containsKey(player.getUUID())) {
            return;
        }

        if (playersVeinMining.contains(player.getUUID())) {
            return;
        }

        playersVeinMining.add(player.getUUID());

        ServerPlayer serverPlayer = (ServerPlayer) player;
        ServerLevel world = serverPlayer.getLevel();
        ItemStack stack = player.getMainHandItem();
        if (!ForgeHooks.isCorrectToolForDrops(state, serverPlayer)) {
            return;
        }

        Block sourceBlock = state.getBlock();

        int maxBlocks = getMaxBlocks(tag);

        int blocks = 0;

        BlockTraverser traverser = getTraverser(tag, world, player, pos);

        if (traverser == null) {
            return;
        }

        while (traverser.hasNext() && blocks < maxBlocks) {
            Tuple<BlockPos, Integer> candidate = traverser.next();
            BlockPos blockPos = candidate.getA();
            int blockDistance = candidate.getB();

            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                return;
            }

            BlockState currentState = world.getBlockState(blockPos);
            Block currentBlock = currentState.getBlock();

            boolean isBlockMatch = isGrid ? true : currentBlock.getRegistryName().equals(sourceBlock.getRegistryName());
            if (!(!currentState.isAir() &&
                isBlockMatch && harvest(serverPlayer, blockPos, pos))) {
                continue;
            }

            traverser.onPosAccepted(candidate.getA(), candidate.getB());
            ++blocks;
        }

        playersVeinMining.remove(player.getUUID());
    }

    public int estimateNumBlocksToExcavate(Player player, CompoundTag tag, BlockState state, BlockPos pos) {
        Level world = player.getLevel();
        ItemStack stack = player.getMainHandItem();

        if (!playersWantingToVeinMine.containsKey(player.getUUID())) {
            return 0;
        }

        if (!ForgeHooks.isCorrectToolForDrops(state, player)) {
            return 0;
        }

        Block sourceBlock = state.getBlock();

        int maxBlocks = getMaxBlocks(tag);
        int blocks = 0;

        BlockTraverser traverser = isGrid ? new GridTraverser(world, player, pos, 3) :
                new VeinTraverser(world, player, pos, 10);

        while (traverser.hasNext() && blocks < maxBlocks) {
            Tuple<BlockPos, Integer> candidate = traverser.next();
            BlockPos blockPos = candidate.getA();
            int blockDistance = candidate.getB();

            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                return blocks;
            }

            BlockState currentState = world.getBlockState(blockPos);
            Block currentBlock = currentState.getBlock();

            boolean isBlockMatch = isGrid ? true : currentBlock.getRegistryName().equals(sourceBlock.getRegistryName());
            if (!(!currentState.isAir() && isBlockMatch)) {
                continue;
            }

            traverser.onPosAccepted(candidate.getA(), candidate.getB());
            ++blocks;
        }
        return blocks;
    }
    @Override
    public float onBreakSpeedCalculation(Player player, CompoundTag tag, PlayerEvent.BreakSpeed event) {
        return 1.f / (1.f + estimateNumBlocksToExcavate(player, tag, event.getState(), event.getPos()));
    }

    @Override
    public Component getTalentDescription(CompoundTag tag, ItemStack stack) {
        if (isGrid) {
            int gridSize = (int) getConfigValueBasedOnLevel(tag, LEConfig.GENERAL.gridMiningSizes.get());
            return new TranslatableComponent(this.descriptionTranslationKey, "`", gridSize);
        }
        return new TranslatableComponent(this.descriptionTranslationKey, "`", getMaxBlocks(tag));
    }
}
