package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataMixin {
    @Shadow @Final public ResourceKey<Level> dimension;

    @ModifyExpressionValue(
            method = "addDecoration",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;NETHER:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> addDecorationNetherKey(
            ResourceKey<Level> original,
            MapDecoration.Type type,
            LevelAccessor levelAccessor
    ) {
        if (levelAccessor != null) {
            MinecraftServer minecraftServer = levelAccessor.getServer();
            if (minecraftServer != null) {
                ServerLevel serverLevel = minecraftServer.getLevel(this.dimension);
                if (serverLevel != null) {
                    if (serverLevel.dimensionTypeId() == BuiltinDimensionTypes.NETHER) {
                        return this.dimension;
                    }
                }
            }
        }

        return original;
    }
}
