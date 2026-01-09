package com.github.mahmudindev.mcmod.dimensionfixer.world;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import com.github.mahmudindev.mcmod.dimensionfixer.config.Config;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DimensionManager {
    public static final ResourceKey<DimensionType> DIRECT_DIMENSION_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            Identifier.fromNamespaceAndPath(DimensionFixer.MOD_ID, "direct")
    );
    private static final Map<Identifier, DimensionAliasData> ALIASES = new HashMap<>();
    private static final Map<Identifier, DimensionTweakData> TWEAKS = new HashMap<>();

    public static void onResourceManagerReload(ResourceManager manager) {
        ALIASES.clear();
        TWEAKS.clear();

        Config config = Config.getConfig();
        config.getAliases().forEach((dimension, alias) -> setAlias(
                Identifier.parse(dimension),
                alias
        ));
        config.getTweaks().forEach((dimension, tweak) -> setTweak(
                Identifier.parse(dimension),
                tweak
        ));

        Gson gson = new Gson();
        manager.listResourceStacks(
                DimensionFixer.MOD_ID,
                identifier -> identifier.getPath().endsWith(".json")
        ).forEach((identifier, resources) -> resources.forEach(resource -> {
            String resourcePath = identifier.getPath().replaceFirst(
                    "^%s/".formatted(DimensionFixer.MOD_ID),
                    ""
            );

            try {
                String dimensionPath = resourcePath
                        .substring(resourcePath.indexOf("/") + 1)
                        .replaceAll("\\.json$", "");

                if (resourcePath.startsWith("alias/")) {
                    setAlias(identifier.withPath(dimensionPath), gson.fromJson(
                            JsonParser.parseReader(resource.openAsReader()),
                            DimensionAliasData.class
                    ));
                } else if (resourcePath.startsWith("tweak/")) {
                    setTweak(identifier.withPath(dimensionPath), gson.fromJson(
                            JsonParser.parseReader(resource.openAsReader()),
                            DimensionTweakData.class
                    ));
                }
            } catch (IOException e) {
                DimensionFixer.LOGGER.error("Failed to read datapack", e);
            }
        }));
    }

    public static Map<Identifier, DimensionAliasData> getAliases() {
        return Map.copyOf(ALIASES);
    }

    public static DimensionAliasData getAlias(Identifier dimension) {
        return ALIASES.get(dimension);
    }

    public static void setAlias(Identifier dimension, DimensionAliasData alias) {
        if (!ALIASES.containsKey(dimension)) {
            ALIASES.put(dimension, new DimensionAliasData());
        }

        DimensionAliasData aliasX = getAlias(dimension);
        aliasX.addAllDimensionType(alias.getDimensionTypes());
        aliasX.addAllDimension(alias.getDimensions());
    }

    public static boolean isAliasDimension(Level dimensionA, ResourceKey<Level> dimensionB) {
        DimensionAliasData alias = getAlias(dimensionB.identifier());
        if (alias != null) {
            if (alias.containDimensionType(getType(dimensionA))) {
                return true;
            }

            return alias.containDimension(dimensionA.dimension());
        }

        return false;
    }

    public static DimensionTweakData getTweak(ResourceKey<Level> dimension) {
        return TWEAKS.get(dimension.identifier());
    }

    public static void setTweak(Identifier dimension, DimensionTweakData tweak) {
        TWEAKS.put(dimension, tweak);
    }

    public static ResourceKey<DimensionType> getType(Level dimension) {
        return dimension.dimensionTypeRegistration().unwrapKey().orElse(DIRECT_DIMENSION_TYPE);
    }
}
