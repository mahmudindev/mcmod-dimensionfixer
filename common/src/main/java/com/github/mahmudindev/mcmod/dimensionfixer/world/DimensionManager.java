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
    private static final Map<ResourceLocation, DimensionTweakData> TWEAKS = new HashMap<>();

    public static void onResourceManagerReload(ResourceManager manager) {
        ALIASES.clear();
        TWEAKS.clear();

        Config config = Config.getConfig();
        config.getAliases().forEach((dimension, alias) -> setAlias(
                new ResourceLocation(dimension),
                alias
        ));
        config.getTweaks().forEach((dimension, tweak) -> setTweak(
                new ResourceLocation(dimension),
                tweak
        ));

        Gson gson = new Gson();
        manager.listResourceStacks(
                DimensionFixer.MOD_ID,
                resourceLocation -> resourceLocation.getPath().endsWith(".json")
        ).forEach((resourceLocation, resources) -> resources.forEach(resource -> {
            String resourcePath = resourceLocation.getPath().replaceFirst(
                    "^%s/".formatted(DimensionFixer.MOD_ID),
                    ""
            );

            try {
                String dimensionPath = resourcePath
                        .substring(resourcePath.indexOf("/") + 1)
                        .replaceAll("\\.json$", "");

                if (resourcePath.startsWith("alias/")) {
                    setAlias(resourceLocation.withPath(dimensionPath), gson.fromJson(
                            JsonParser.parseReader(resource.openAsReader()),
                            DimensionAliasData.class
                    ));
                } else if (resourcePath.startsWith("tweak/")) {
                    setTweak(resourceLocation.withPath(dimensionPath), gson.fromJson(
                            JsonParser.parseReader(resource.openAsReader()),
                            DimensionTweakData.class
                    ));
                }
            } catch (IOException e) {
                DimensionFixer.LOGGER.error("Failed to read datapack", e);
            }
        }));
    }

    public static Map<ResourceLocation, DimensionAliasData> getAliases() {
        return Map.copyOf(ALIASES);
    }

    public static DimensionAliasData getAlias(ResourceLocation dimension) {
        return ALIASES.get(dimension);
    }

    public static void setAlias(ResourceLocation dimension, DimensionAliasData alias) {
        if (!ALIASES.containsKey(dimension)) {
            ALIASES.put(dimension, new DimensionAliasData());
        }

        DimensionAliasData aliasX = getAlias(dimension);
        aliasX.addAllDimensionType(alias.getDimensionTypes());
        aliasX.addAllDimension(alias.getDimensions());
    }

    public static boolean isAliasDimension(Level dimensionA, ResourceKey<Level> dimensionB) {
        DimensionAliasData alias = getAlias(dimensionB.location());
        if (alias != null) {
            if (alias.containDimensionType(dimensionA.dimensionTypeId())) {
                return true;
            }

            return alias.containDimension(dimensionA.dimension());
        }

        return false;
    }

    public static DimensionTweakData getTweak(ResourceKey<Level> dimension) {
        return TWEAKS.get(dimension.location());
    }

    public static void setTweak(ResourceLocation dimension, DimensionTweakData tweak) {
        TWEAKS.put(dimension, tweak);
    }
}
