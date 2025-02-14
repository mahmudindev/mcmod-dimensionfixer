package com.github.mahmudindev.mcmod.dimensionfixer.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Shadow public abstract ServerLevel serverLevel();

    @ModifyExpressionValue(
            method = "changeDimension",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;",
                    ordinal = 1
            )
    )
    private ResourceKey<Level> changeDimensionModifyNetherTrigger0(
            ResourceKey<Level> original
    ) {
        ServerLevel serverLevel = this.serverLevel();
        if (serverLevel.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD) {
            return serverLevel.dimension();
        } else if (serverLevel.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD_CAVES) {
            return serverLevel.dimension();
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "changeDimension",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;NETHER:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> changeDimensionModifyNetherTrigger1(
            ResourceKey<Level> original,
            ServerLevel serverLevel
    ) {
        if (serverLevel.dimensionTypeId() == BuiltinDimensionTypes.NETHER) {
            return serverLevel.dimension();
        }

        return original;
    }
}
