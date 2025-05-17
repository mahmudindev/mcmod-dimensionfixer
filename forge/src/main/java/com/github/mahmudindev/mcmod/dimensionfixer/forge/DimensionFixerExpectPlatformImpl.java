package com.github.mahmudindev.mcmod.dimensionfixer.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class DimensionFixerExpectPlatformImpl {
    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
