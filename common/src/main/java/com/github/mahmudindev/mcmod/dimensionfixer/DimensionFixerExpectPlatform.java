package com.github.mahmudindev.mcmod.dimensionfixer;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class DimensionFixerExpectPlatform {
    @ExpectPlatform
    public static Path getConfigDir() {
        return Path.of(".");
    }
}
