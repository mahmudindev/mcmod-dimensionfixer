package com.github.mahmudindev.mcmod.dimensionfixer.forge.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    @Shadow private Vec3 enteredNetherPosition;
    @Unique
    private Vec3 previousEnteredNetherPosition;

    public ServerPlayerMixin(
            Level level,
            BlockPos blockPos,
            float yRot,
            GameProfile gameProfile
    ) {
        super(level, blockPos, yRot, gameProfile);
    }

    @Shadow public abstract ServerLevel serverLevel();

    @WrapOperation(
            method = "changeDimension",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/ITeleporter;placeEntity(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerLevel;FLjava/util/function/Function;)Lnet/minecraft/world/entity/Entity;"
            ),
            remap = false
    )
    private Entity changeDimensionNetherTrigger(
            ITeleporter instance,
            Entity entity,
            ServerLevel currentWorld,
            ServerLevel destWorld,
            float yaw,
            Function<Boolean, Entity> repositionEntity,
            Operation<Entity> original
    ) {
        Function<Boolean, Entity> modifiedRepositionEntity = spawnPortal -> {
            this.previousEnteredNetherPosition = this.enteredNetherPosition;

            return repositionEntity.apply(spawnPortal);
        };

        return original.call(
                instance,
                entity,
                currentWorld,
                destWorld,
                yaw,
                modifiedRepositionEntity
        );
    }

    @WrapMethod(method = "triggerDimensionChangeTriggers")
    private void triggerDimensionChangeTriggersNetherTrigger(
            ServerLevel serverLevel,
            Operation<Void> original
    ) {
        if (this.previousEnteredNetherPosition != null) {
            if (DimensionManager.isAlias(
                    this.serverLevel(),
                    Level.OVERWORLD
            ) && DimensionManager.isAlias(
                    serverLevel,
                    Level.NETHER
            )) {
                this.enteredNetherPosition = this.position();
            } else {
                this.enteredNetherPosition = this.previousEnteredNetherPosition;
            }

            this.previousEnteredNetherPosition = null;
        }

        original.call(serverLevel);
    }
}
