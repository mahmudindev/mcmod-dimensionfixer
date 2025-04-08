package com.github.mahmudindev.mcmod.dimensionfixer.neoforge;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class DimensionFixerExpectPlatformImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
