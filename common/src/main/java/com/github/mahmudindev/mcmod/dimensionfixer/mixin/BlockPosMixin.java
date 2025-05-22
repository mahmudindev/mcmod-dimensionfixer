package com.github.mahmudindev.mcmod.dimensionfixer.mixin;

import com.github.mahmudindev.mcmod.dimensionfixer.base.IBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockPos.class)
public abstract class BlockPosMixin implements IBlockPos {
    @Unique
    private Level level;

    @Override
    public Level dimensionfixer$getLevel() {
        return this.level;
    }

    @Override
    public void dimensionfixer$setLevel(Level level) {
        this.level = level;
    }
}
