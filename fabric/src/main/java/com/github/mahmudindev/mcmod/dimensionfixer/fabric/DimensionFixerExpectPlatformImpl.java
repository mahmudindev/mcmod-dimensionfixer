package com.github.mahmudindev.mcmod.dimensionfixer.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class DimensionFixerExpectPlatformImpl {
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
