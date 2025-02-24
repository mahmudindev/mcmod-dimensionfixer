package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocationPredicate.class)
public abstract class LocationPredicateMixin {
    @Shadow @Final private ResourceKey<Level> dimension;

    @WrapOperation(
            method = "matches",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;dimension()Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> matchesDimensionMask(
            ServerLevel instance,
            Operation<ResourceKey<Level>> original
    ) {
        if (DimensionManager.isAlias(instance, this.dimension)) {
            return this.dimension;
        }

        return original.call(instance);
    }
}
