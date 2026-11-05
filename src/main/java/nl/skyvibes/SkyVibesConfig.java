package nl.skyvibes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SkyVibesConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("skyvibes.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject data = new JsonObject();
    private static boolean galaxyEnabled = true;

    private SkyVibesConfig() {}

    public static void load() {
        if (Files.exists(PATH)) {
            try {
                var parsed = JsonParser.parseString(Files.readString(PATH, StandardCharsets.UTF_8));
                if (parsed.isJsonObject()) {
                    data = parsed.getAsJsonObject();
                }
            } catch (Exception e) {
                System.err.println("[SkyVibes] Could not read config: " + e.getMessage());
            }
        }
        try {
            if (data.has("galaxyEnabled")) {
                galaxyEnabled = data.get("galaxyEnabled").getAsBoolean();
            } else if (data.has("skyTheme")) {
                galaxyEnabled = "galaxy".equalsIgnoreCase(data.get("skyTheme").getAsString());
            }
        } catch (Exception ignored) {
            galaxyEnabled = true;
        }
        save();
    }

    public static boolean isGalaxyEnabled() {
        return galaxyEnabled;
    }

    public static void setGalaxyEnabled(boolean enabled) {
        galaxyEnabled = enabled;
        save();
    }

    private static void save() {
        data.addProperty("galaxyEnabled", galaxyEnabled);
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[SkyVibes] Could not save config: " + e.getMessage());
        }
    }
}
