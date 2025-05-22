package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.base.IBlockPos;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionTweakData;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
    @Shadow @Final private ServerLevel level;

    @ModifyExpressionValue(
            method = "findClosestPortalPosition",
            at = @At(value = "CONSTANT", args = "intValue=16")
    )
    private int findClosestPortalPositionScale16(int original, BlockPos blockPos) {
        Level levelX = ((IBlockPos) blockPos).dimensionfixer$getLevel();
        if (levelX != null) {
            DimensionTweakData tweak = DimensionManager.getTweak(this.level.dimension());
            if (tweak != null) {
                Boolean fixPortalSearchRadius = tweak.getFixPortalSearchRadius();
                if (fixPortalSearchRadius != null && !fixPortalSearchRadius) {
                    return original;
                }

                double scaleA = this.level.dimensionType().coordinateScale();
                double scaleB = levelX.dimensionType().coordinateScale();
                return (int) Math.round((scaleA * scaleB / scaleA) * 16);
            }
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "findClosestPortalPosition",
            at = @At(value = "CONSTANT", args = "intValue=128")
    )
    private int findClosestPortalPositionScale128(int original, BlockPos blockPos) {
        Level levelX = ((IBlockPos) blockPos).dimensionfixer$getLevel();
        if (levelX != null) {
            DimensionTweakData tweak = DimensionManager.getTweak(this.level.dimension());
            if (tweak != null) {
                Boolean fixPortalSearchRadius = tweak.getFixPortalSearchRadius();
                if (fixPortalSearchRadius != null && !fixPortalSearchRadius) {
                    return original;
                }

                double scaleA = this.level.dimensionType().coordinateScale();
                double scaleB = levelX.dimensionType().coordinateScale();
                return (int) Math.round((scaleA * scaleB / scaleA) * 16);
            }
        }

        return original;
    }
}
