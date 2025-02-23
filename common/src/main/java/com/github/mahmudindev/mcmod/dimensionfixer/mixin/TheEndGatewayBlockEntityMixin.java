package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TheEndGatewayBlockEntity.class)
public abstract class TheEndGatewayBlockEntityMixin {
    @ModifyExpressionValue(
            method = "teleportEntity",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;END:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private static ResourceKey<Level> teleportEntityEndKey(
            ResourceKey<Level> original,
            Level level
    ) {
        if (level.dimensionTypeId() == BuiltinDimensionTypes.END) {
            return level.dimension();
        }

        return original;
    }
}
