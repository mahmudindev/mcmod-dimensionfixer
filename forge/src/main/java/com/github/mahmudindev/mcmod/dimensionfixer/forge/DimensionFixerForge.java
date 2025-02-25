package com.github.mahmudindev.mcmod.dimensionfixer.forge;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(DimensionFixer.MOD_ID)
public final class DimensionFixerForge {
    public DimensionFixerForge() {
        DimensionFixer.CONFIG_DIR = FMLPaths.CONFIGDIR.get();

        // Run our common setup.
        DimensionFixer.init();

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(this.onResourceManagerReload());
        });
    }

    private ResourceManagerReloadListener onResourceManagerReload() {
        return DimensionFixer::onResourceManagerReload;
    }
}
