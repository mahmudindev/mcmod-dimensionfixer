package com.github.mahmudindev.mcmod.dimensionfixer.base;

import net.minecraft.world.level.Level;

public interface IBlockPos {
    Level dimensionfixer$getLevel();

    void dimensionfixer$setLevel(Level level);
}
