package com.github.mahmudindev.mcmod.dimensionfixer;

import com.github.mahmudindev.mcmod.dimensionfixer.config.Config;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionManager;
import com.github.mahmudindev.mcmod.orenoevents.event.events.ServerEvents;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class DimensionFixer {
    public static final String MOD_ID = "dimensionfixer";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        Config.load();

        ServerEvents.RESOURCE_MANAGER_RELOAD.register(resourceManager -> {
            DimensionManager.onServerResourceManagerReload(resourceManager);
        });
    }
}
