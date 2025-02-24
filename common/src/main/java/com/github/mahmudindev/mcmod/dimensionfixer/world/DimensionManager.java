package com.github.mahmudindev.mcmod.dimensionfixer.world;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import com.github.mahmudindev.mcmod.dimensionfixer.config.Config;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DimensionManager {
    private static final Map<ResourceLocation, DimensionAliasData> ALIASES = new HashMap<>();

    public static void onResourceManagerReload(ResourceManager manager) {
        ALIASES.clear();

        Config config = Config.getConfig();
        config.getAliases().forEach((dimension, dimensionAliasData) -> setAlias(
                new ResourceLocation(dimension),
                dimensionAliasData
        ));

        Gson gson = new Gson();
        manager.listResources(
                DimensionFixer.MOD_ID,
                resourceLocation -> resourceLocation.getPath().endsWith(".json")
        ).forEach((resourceLocation, resource) -> {
            String resourcePath = resourceLocation.getPath().replaceFirst(
                    "^%s/".formatted(DimensionFixer.MOD_ID),
                    ""
            );

            if (!resourcePath.startsWith("alias/")) {
                return;
            }

            try {
                String dimensionPath = resourcePath
                        .substring(resourcePath.lastIndexOf("/") + 1)
                        .replaceAll("\\.json$", "");

                setAlias(resourceLocation.withPath(dimensionPath), gson.fromJson(
                        JsonParser.parseReader(resource.openAsReader()),
                        DimensionAliasData.class
                ));
            } catch (IOException e) {
                DimensionFixer.LOGGER.error("Failed to read datapack", e);
            }
        });
    }

    public static Map<ResourceLocation, DimensionAliasData> getAliases() {
        return Map.copyOf(ALIASES);
    }

    public static void setAlias(
            ResourceLocation dimension,
            DimensionAliasData dimensionAliasData
    ) {
        if (!ALIASES.containsKey(dimension)) {
            ALIASES.put(dimension, new DimensionAliasData());
        }

        DimensionAliasData dimensionAliasDataX = ALIASES.get(dimension);
        dimensionAliasDataX.addAllDimensionType(dimensionAliasData.getDimensionTypes());
        dimensionAliasDataX.addAllDimension(dimensionAliasData.getDimensions());
    }

    public static boolean isAlias(
            Level dimensionA,
            ResourceKey<Level> dimensionB
    ) {
        DimensionAliasData alias = ALIASES.get(dimensionB.location());
        if (alias != null) {
            if (alias.containDimensionType(dimensionA.dimensionTypeId())) {
                return true;
            }

            return alias.containDimension(dimensionA.dimension());
        }

        return false;
    }
}
