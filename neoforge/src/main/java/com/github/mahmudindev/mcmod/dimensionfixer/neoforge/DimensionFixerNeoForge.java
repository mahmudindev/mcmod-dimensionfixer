package com.github.mahmudindev.mcmod.dimensionfixer.neoforge;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Mod(DimensionFixer.MOD_ID)
public final class DimensionFixerNeoForge {
    public DimensionFixerNeoForge() {
        // Run our common setup.
        DimensionFixer.init();

        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new ResourceManagerReloadListener() {
                @Override
                public void onResourceManagerReload(ResourceManager resourceManager) {
                    DimensionFixer.onResourceManagerReload(resourceManager);
                }
            });
        });
    }
}
