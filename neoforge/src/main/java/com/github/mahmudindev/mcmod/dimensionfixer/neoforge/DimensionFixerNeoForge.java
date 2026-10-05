package com.github.mahmudindev.mcmod.dimensionfixer.neoforge;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import net.neoforged.fml.common.Mod;

@Mod(DimensionFixer.MOD_ID)
public final class DimensionFixerNeoForge {
    public DimensionFixerNeoForge() {
        // Run our common setup.
        DimensionFixer.init();
    }
}
