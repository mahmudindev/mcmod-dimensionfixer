package com.github.mahmudindev.mcmod.dimensionfixer;

import com.github.mahmudindev.mcmod.dimensionfixer.config.Config;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.mojang.logging.LogUtils;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class DimensionFixer {
    public static final String MOD_ID = "dimensionfixer";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Path CONFIG_DIR = Path.of(".");

    public static void init() {
        Config.load();
    }

    public static void onResourceManagerReload(ResourceManager manager) {
        DimensionManager.onResourceManagerReload(manager);
    }
}
