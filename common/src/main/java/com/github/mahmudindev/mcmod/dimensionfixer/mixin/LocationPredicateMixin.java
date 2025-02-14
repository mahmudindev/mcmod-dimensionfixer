package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
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
    private ResourceKey<Level> matchesDimension(
            ServerLevel instance,
            Operation<ResourceKey<Level>> original
    ) {
        if (this.dimension == Level.OVERWORLD) {
            if (instance.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD) {
                return Level.OVERWORLD;
            } else if (instance.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD_CAVES) {
                return Level.OVERWORLD;
            }
        } else if (this.dimension == Level.NETHER) {
            if (instance.dimensionTypeId() == BuiltinDimensionTypes.NETHER) {
                return Level.NETHER;
            }
        } else if (this.dimension == Level.END) {
            if (instance.dimensionTypeId() == BuiltinDimensionTypes.END) {
                return Level.END;
            }
        }

        return original.call(instance);
    }
}
