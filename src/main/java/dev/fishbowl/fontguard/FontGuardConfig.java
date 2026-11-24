package dev.fishbowl.fontguard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FontGuardConfig {

    public boolean enabled = true;
    public boolean showWarning = true;
    public List<String> trustedServers = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static FontGuardConfig load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("fontguard.json");
        if (Files.exists(path)) {
            try {
                FontGuardConfig loaded = GSON.fromJson(Files.readString(path), FontGuardConfig.class);
                if (loaded != null) {
                    if (loaded.trustedServers == null) {
                        loaded.trustedServers = new ArrayList<>();
                    }
                    return loaded;
                }
            } catch (Exception e) {
                FontGuard.LOGGER.warn("Could not read fontguard.json, using defaults", e);
            }
        }
        FontGuardConfig fresh = new FontGuardConfig();
        fresh.save();
        return fresh;
    }

    public void save() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("fontguard.json");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(this));
        } catch (IOException e) {
            FontGuard.LOGGER.warn("Could not write fontguard.json", e);
        }
    }
}
