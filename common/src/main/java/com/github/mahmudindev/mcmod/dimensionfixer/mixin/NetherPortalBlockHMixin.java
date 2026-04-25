package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = NetherPortalBlock.class, priority = 1500)
public class NetherPortalBlockHMixin {
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
            @Local(name = "newLevel") ServerLevel newLevel
    ) {
        if (DimensionManager.isAliasDimension(newLevel, Level.NETHER)) {
            return newLevel.dimension();
        }

        return original;
    }
}
