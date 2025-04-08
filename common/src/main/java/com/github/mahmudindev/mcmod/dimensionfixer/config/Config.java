package com.github.mahmudindev.mcmod.dimensionfixer.config;

import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixer;
import com.github.mahmudindev.mcmod.dimensionfixer.DimensionFixerExpectPlatform;
import com.github.mahmudindev.mcmod.dimensionfixer.world.DimensionAliasData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Path CONFIG_DIR = DimensionFixerExpectPlatform.getConfigDir();
    private static Config CONFIG = new Config();

    private final Map<String, DimensionAliasData> aliases = new HashMap<>();

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
    }

    public Map<String, DimensionAliasData> getAliases() {
        return this.aliases;
    }

    public static void load() {
        Gson parser = new GsonBuilder().setPrettyPrinting().create();

        File configFile = CONFIG_DIR.resolve(DimensionFixer.MOD_ID + ".json").toFile();
        if (!configFile.exists()) {
            CONFIG.defaults();

            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(parser.toJson(CONFIG));
            } catch (IOException e) {
                DimensionFixer.LOGGER.error("Failed to write config", e);
            }
        } else {
            try (FileReader reader = new FileReader(configFile)) {
                CONFIG = parser.fromJson(reader, Config.class);
            } catch (IOException e) {
                DimensionFixer.LOGGER.error("Failed to read config", e);
            }
        }
    }

    public static Config getConfig() {
        return CONFIG;
    }
}
