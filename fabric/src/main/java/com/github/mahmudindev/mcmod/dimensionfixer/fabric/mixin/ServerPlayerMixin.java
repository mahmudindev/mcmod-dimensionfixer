package com.github.mahmudindev.mcmod.dimensionfixer.fabric.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
    private ResourceKey<Level> changeDimensionNetherTrigger0(
            ResourceKey<Level> original
    ) {
        ServerLevel serverLevel = this.serverLevel();
        if (DimensionManager.isAlias(serverLevel, Level.OVERWORLD)) {
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
    private ResourceKey<Level> changeDimensionNetherTrigger1(
            ResourceKey<Level> original,
            ServerLevel serverLevel
    ) {
        if (DimensionManager.isAlias(serverLevel, Level.NETHER)) {
            return serverLevel.dimension();
        }

        return original;
    }
}
