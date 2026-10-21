package com.misanthropy.linggango.shading_begone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShadingConfig {
    private static final Logger LOGGER = LogManager.getLogger(ShadingConfig.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final ShadingConfig INSTANCE = new ShadingConfig();

    public static volatile boolean blockShadingEffective = false;
    public static volatile boolean cloudShadingEffective = false;
    public static volatile boolean entityShadingEffective = false;
    public static volatile boolean cloudGeometryDirty = false;

    public boolean blockShadingEnabled = false;
    public boolean cloudShadingEnabled = false;
    public boolean entityShadingEnabled = false;

    public transient boolean shadersActive = false;

    private static Path configPath() {
        return FMLPaths.CONFIGDIR.get().resolve("shading_begone.json");
    }

    public static void updateEffectiveStates() {
        boolean shaders = INSTANCE.shadersActive;
        blockShadingEffective = INSTANCE.blockShadingEnabled || shaders;
        entityShadingEffective = INSTANCE.entityShadingEnabled || shaders;

        boolean clouds = INSTANCE.cloudShadingEnabled || shaders;
        if (clouds != cloudShadingEffective) {
            cloudShadingEffective = clouds;
            cloudGeometryDirty = true;
        }
    }

    public static void load() {
        try {
            Path path = configPath();
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    ShadingConfig loaded = GSON.fromJson(reader, ShadingConfig.class);
                    if (loaded != null) {
                        INSTANCE.blockShadingEnabled = loaded.blockShadingEnabled;
                        INSTANCE.cloudShadingEnabled = loaded.cloudShadingEnabled;
                        INSTANCE.entityShadingEnabled = loaded.entityShadingEnabled;
                    }
                }
                updateEffectiveStates();
            } else {
                save();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load shading_begone config", e);
            updateEffectiveStates();
        }
    }

    public static void save() {
        updateEffectiveStates();
        try {
            Path path = configPath();
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save shading_begone config", e);
        }
    }
}
