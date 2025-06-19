package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    private ServerPlayerMixin(
            Level level,
            BlockPos blockPos,
            float yRot,
            GameProfile gameProfile
    ) {
        super(level, blockPos, yRot, gameProfile);
    }

    @Shadow public abstract ServerLevel serverLevel();

    @ModifyExpressionValue(
            method = "changeDimension",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;OVERWORLD:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> changeDimensionNetherTrigger0(
            ResourceKey<Level> original
    ) {
        ServerLevel serverLevel = this.serverLevel();
        if (DimensionManager.isAliasDimension(serverLevel, Level.OVERWORLD)) {
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
            DimensionTransition dimensionTransition
    ) {
        ServerLevel serverLevel = dimensionTransition.newLevel();
        if (DimensionManager.isAliasDimension(serverLevel, Level.NETHER)) {
            return serverLevel.dimension();
        }

        return original;
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
        ResourceKey<Level> dimensionA0 = serverLevel.dimension();
        List<ResourceKey<Level>> dimensionA1List = new LinkedList<>();
        ResourceKey<Level> dimensionB0 = this.level().dimension();
        List<ResourceKey<Level>> dimensionB1List = new LinkedList<>();

        ResourceKey<DimensionType> dimensionTypeA0 = DimensionManager.getType(serverLevel);
        ResourceKey<DimensionType> dimensionTypeB0 = DimensionManager.getType(this.level());
        DimensionManager.getAliases().forEach((k, v) -> {
            if (v.containDimension(dimensionA0) || v.containDimensionType(dimensionTypeA0)) {
                dimensionA1List.add(ResourceKey.create(Registries.DIMENSION, k));
            }

            if (v.containDimension(dimensionB0) || v.containDimensionType(dimensionTypeB0)) {
                dimensionB1List.add(ResourceKey.create(Registries.DIMENSION, k));
            }
        });

        dimensionA1List.forEach(dimensionA -> dimensionB1List.forEach(dimensionB -> {
            if (dimensionA == dimensionA0 && dimensionB == dimensionB0) {
                return;
            }

            CriteriaTriggers.CHANGED_DIMENSION.trigger(
                    (ServerPlayer) (Object) this,
                    dimensionA,
                    dimensionB
            );
        }));
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
        if (DimensionManager.isAliasDimension(serverLevel, Level.NETHER)) {
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
        if (DimensionManager.isAliasDimension(level, Level.OVERWORLD)) {
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
        if (DimensionManager.isAliasDimension(level, Level.NETHER)) {
            return level.dimension();
        }

        return original;
    }
}
