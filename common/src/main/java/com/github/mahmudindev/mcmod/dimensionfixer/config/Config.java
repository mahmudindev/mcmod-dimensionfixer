package com.github.mahmudindev.mcmod.dimensionfixer.config;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionAliasData;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionTweakData;
import com.github.mahmudindev.mcmod.orenocommons.platform.UnifiedPlatform;
import com.github.mahmudindev.mcmod.orenoconfig.config.configs.ModCommonConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Path CONFIG_DIR = UnifiedPlatform.getConfigDir();
    private static Config CONFIG = new Config();

    private final Map<String, DimensionAliasData> aliases = new HashMap<>();
    private final Map<String, DimensionTweakData> tweaks = new HashMap<>();

    private void defaults() {
        DimensionAliasData alias0 = new DimensionAliasData();
        alias0.addDimensionType(BuiltinDimensionTypes.OVERWORLD);
        alias0.addDimensionType(BuiltinDimensionTypes.OVERWORLD_CAVES);
        this.aliases.put(String.valueOf(Level.OVERWORLD.location()), alias0);

        DimensionAliasData alias1 = new DimensionAliasData();
        alias1.addDimensionType(BuiltinDimensionTypes.NETHER);
        this.aliases.put(String.valueOf(Level.NETHER.location()), alias1);

        DimensionAliasData alias2 = new DimensionAliasData();
        alias2.addDimensionType(BuiltinDimensionTypes.END);
        this.aliases.put(String.valueOf(Level.END.location()), alias2);

        DimensionTweakData tweak0 = new DimensionTweakData();
        tweak0.setFixSleeping(true);
        tweak0.setFixPortalSearchRadius(true);
        tweak0.setOverrideFlatCheck(true);
        this.tweaks.put(DimensionFixer.MOD_ID + ":dimension", tweak0);
    }

    public Map<String, DimensionAliasData> getAliases() {
        return this.aliases;
    }

    public Map<String, DimensionTweakData> getTweaks() {
        return this.tweaks;
    }

    public static void load() {
        File oldConfigFile = CONFIG_DIR.resolve(DimensionFixer.MOD_ID + ".json").toFile();
        if (oldConfigFile.exists()) {
            try (FileReader reader = new FileReader(oldConfigFile)) {
                Gson gson = new GsonBuilder().create();
                CONFIG = gson.fromJson(reader, Config.class);
            } catch (IOException e) {
                DimensionFixer.LOGGER.error("Failed to read config", e);
            }

            oldConfigFile.delete();
        } else {
            CONFIG.defaults();
        }

        ModCommonConfig config = new ModCommonConfig(DimensionFixer.MOD_ID, "dimensionfixer");
        config.registerPojo("", CONFIG);
        config.load();
    }

    public static Config getConfig() {
        return CONFIG;
    }
}
