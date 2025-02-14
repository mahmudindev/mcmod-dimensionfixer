package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(
            Level level,
            BlockPos blockPos,
            float yRot,
            GameProfile gameProfile
    ) {
        super(level, blockPos, yRot, gameProfile);
    }

    @Inject(
            method = "triggerDimensionChangeTriggers",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/critereon/ChangeDimensionTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;)V"
            )
    )
    private void triggerDimensionChangeTriggersTrigger(
            ServerLevel serverLevel,
            CallbackInfo ci
    ) {
        ResourceKey<Level> dimensionA = serverLevel.dimension();
        ResourceKey<DimensionType> dimensionTypeA = serverLevel.dimensionTypeId();
        if (dimensionTypeA == BuiltinDimensionTypes.OVERWORLD) {
            dimensionA = Level.OVERWORLD;
        } else if (dimensionTypeA == BuiltinDimensionTypes.OVERWORLD_CAVES) {
            dimensionA = Level.OVERWORLD;
        } else if (dimensionTypeA == BuiltinDimensionTypes.NETHER) {
            dimensionA = Level.NETHER;
        } else if (dimensionTypeA == BuiltinDimensionTypes.END) {
            dimensionA = Level.END;
        }

        Level level = this.level();

        ResourceKey<Level> dimensionB = level.dimension();
        ResourceKey<DimensionType> dimensionTypeB = level.dimensionTypeId();
        if (dimensionTypeB == BuiltinDimensionTypes.OVERWORLD) {
            dimensionB = Level.OVERWORLD;
        } else if (dimensionTypeA == BuiltinDimensionTypes.OVERWORLD_CAVES) {
            dimensionB = Level.OVERWORLD;
        } else if (dimensionTypeB == BuiltinDimensionTypes.NETHER) {
            dimensionB = Level.NETHER;
        } else if (dimensionTypeB == BuiltinDimensionTypes.END) {
            dimensionB = Level.END;
        }

        if (dimensionA == serverLevel.dimension() && dimensionB == level.dimension()) {
            return;
        }

        CriteriaTriggers.CHANGED_DIMENSION.trigger(
                (ServerPlayer) (Object) this,
                dimensionA,
                dimensionB
        );
    }

    @ModifyExpressionValue(
            method = "triggerDimensionChangeTriggers",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;NETHER:Lnet/minecraft/resources/ResourceKey;",
                    ordinal = 0
            )
    )
    private ResourceKey<Level> triggerDimensionChangeTriggersNetherKey0(
            ResourceKey<Level> original,
            ServerLevel serverLevel
    ) {
        if (serverLevel.dimensionTypeId() == BuiltinDimensionTypes.NETHER) {
            return serverLevel.dimension();
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "triggerDimensionChangeTriggers",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> triggerDimensionChangeTriggersOverworldKey(
            ResourceKey<Level> original
    ) {
        Level level = this.level();
        if (level.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD) {
            return level.dimension();
        } else if (level.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD_CAVES) {
            return level.dimension();
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "triggerDimensionChangeTriggers",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;NETHER:Lnet/minecraft/resources/ResourceKey;",
                    ordinal = 1
            )
    )
    private ResourceKey<Level> triggerDimensionChangeTriggersNetherKey1(
            ResourceKey<Level> original
    ) {
        Level level = this.level();
        if (level.dimensionTypeId() == BuiltinDimensionTypes.NETHER) {
            return level.dimension();
        }

        return original;
    }
}
