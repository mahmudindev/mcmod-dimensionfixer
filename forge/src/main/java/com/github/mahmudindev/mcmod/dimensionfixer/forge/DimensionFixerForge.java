package com.github.mahmudindev.mcmod.dimensionfixer.forge;

import net.minecraftforge.fml.common.Mod;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;

@Mod(DimensionFixer.MOD_ID)
public final class DimensionFixerForge {
    public DimensionFixerForge() {
        // Run our common setup.
        DimensionFixer.init();
    }
}
