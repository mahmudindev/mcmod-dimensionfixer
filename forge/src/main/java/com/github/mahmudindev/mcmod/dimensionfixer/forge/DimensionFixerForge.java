package com.github.mahmudindev.mcmod.dimensionfixer.forge;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import net.minecraftforge.fml.common.Mod;

@Mod(DimensionFixer.MOD_ID)
public final class DimensionFixerForge {
    public DimensionFixerForge() {
        // Run our common setup.
        DimensionFixer.init();
    }
}
