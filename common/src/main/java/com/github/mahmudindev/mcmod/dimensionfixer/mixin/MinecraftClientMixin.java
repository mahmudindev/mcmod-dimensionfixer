package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public LocalPlayer player;

    @ModifyExpressionValue(
            method = "getSituationalMusic",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;END:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> getSituationalMusicModifyEndKey(
            ResourceKey<Level> original
    ) {
        Level level = this.player.level();
        if (level.dimensionTypeId() == BuiltinDimensionTypes.END) {
            return level.dimension();
        }

        return original;
    }

    @ModifyExpressionValue(
            method = "getSituationalMusic",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;NETHER:Lnet/minecraft/resources/ResourceKey;"
            )
    )
    private ResourceKey<Level> getSituationalMusicModifyNetherKey(
            ResourceKey<Level> original
    ) {
        Level level = this.player.level();
        if (level.dimensionTypeId() == BuiltinDimensionTypes.NETHER) {
            return level.dimension();
        }

        return original;
    }
}
