package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.base.IBlockPos;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionTweakData;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
    @ModifyExpressionValue(
            method = "getPortalDestination",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;NETHER:Lnet/minecraft/resources/ResourceKey;",
                    ordinal = 2
            )
    )
    private ResourceKey<Level> getPortalDestinationNetherKey2(
            ResourceKey<Level> original,
            @Local(ordinal = 1) ServerLevel serverLevel
    ) {
        if (DimensionManager.isAlias(serverLevel, Level.NETHER)) {
            return serverLevel.dimension();
        }

        return original;
    }

    @WrapOperation(
            method = "getExitPortal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/portal/PortalForcer;findClosestPortalPosition(Lnet/minecraft/core/BlockPos;ZLnet/minecraft/world/level/border/WorldBorder;)Ljava/util/Optional;"
            )
    )
    private Optional<BlockPos> getExitPortalfindClosestPortalPositionPrepare(
            PortalForcer instance,
            BlockPos blockPos,
            boolean isNether,
            WorldBorder worldBorder,
            Operation<Optional<BlockPos>> original,
            ServerLevel serverLevel,
            Entity entity
    ) {
        DimensionTweakData tweak = DimensionManager.getTweak(serverLevel.dimension());
        if (tweak != null) {
            ((IBlockPos) blockPos).dimensionfixer$setLevel(entity.level());
        }

        return original.call(instance, blockPos, isNether, worldBorder);
    }
}
