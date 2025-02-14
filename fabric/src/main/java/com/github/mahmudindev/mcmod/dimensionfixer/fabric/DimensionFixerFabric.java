package com.github.mahmudindev.mcmod.dimensionfixer.fabric;

import net.fabricmc.api.ModInitializer;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;

public final class DimensionFixerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        DimensionFixer.init();
    }
}
