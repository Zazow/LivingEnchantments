package com.zazow.livingenchantments.mixin;

import com.zazow.livingenchantments.attributes.SaviorTalent;
import com.zazow.livingenchantments.attributes.Talent;
import com.zazow.livingenchantments.util.LEUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(
            method = "checkTotemDeathProtection",
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private ItemStack getChanceToSavePlayer(ItemStack inStack) {
        if (inStack != null) {
            return inStack;
        }

        if (!((Object) this instanceof Player)) {
            return null;
        }

        Player player = (Player) (Object) this;

        for (ItemStack stack : player.getArmorSlots()) {
            CompoundTag tag = LEUtil.getLivingEnchantmentTag(stack);
            if (tag == null) {
                continue;
            }

            Talent talent = Talent.getTalent(tag, false);
            if (!(talent instanceof SaviorTalent)) {
                continue;
            }

            if (((SaviorTalent) talent).checkTotemDeathProtection(tag, stack, player)) {
                return stack;
            }
        }

        return null;
    }
//
//    @Inject(
//            method = "checkTotemDeathProtection",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V",
//                    shift = At.Shift.AFTER
//            )
//    )
//    private void injected(DamageSource p_21263_, CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(true);
//    }

}
