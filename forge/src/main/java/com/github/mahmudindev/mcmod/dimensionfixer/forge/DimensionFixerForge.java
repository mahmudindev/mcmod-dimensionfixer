package com.github.mahmudindev.mcmod.dimensionfixer.forge;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(DimensionFixer.MOD_ID)
public final class DimensionFixerForge {
    public DimensionFixerForge() {
        // Run our common setup.
        DimensionFixer.init();

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new ResourceManagerReloadListener() {
                @Override
                public void onResourceManagerReload(ResourceManager resourceManager) {
                    DimensionFixer.onResourceManagerReload(resourceManager);
                }
            });
        });
    }
}
