package nl.skyvibes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Client-only preferences. Keeps any earlier SkyVibes config entries intact. */
public final class SkyVibesConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("skyvibes.json");
    private static JsonObject data = new JsonObject();
    private static boolean skyEnabled = true;
    private static boolean starsEnabled = true;
    private static boolean nebulaEnabled = true;
    private static boolean planetsEnabled = true;
    private static String theme = "Galaxy";

    private SkyVibesConfig() {}

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                var parsed = JsonParser.parseString(Files.readString(FILE, StandardCharsets.UTF_8));
                if (parsed.isJsonObject()) data = parsed.getAsJsonObject();
            }
        } catch (Exception ex) { System.err.println("[SkyVibes] Invalid config: " + ex); }
        skyEnabled = bool("skyEnabled", bool("galaxyEnabled", true));
        starsEnabled = bool("starsEnabled", true);
        nebulaEnabled = bool("nebulaEnabled", true);
        planetsEnabled = bool("planetsEnabled", true);
        theme = str("skyTheme", "Galaxy");
        if (theme.length() > 0) theme = Character.toUpperCase(theme.charAt(0)) + theme.substring(1).toLowerCase();
        save();
    }

    private static boolean bool(String name, boolean fallback) {
        try { return data.has(name) ? data.get(name).getAsBoolean() : fallback; }
        catch (Exception ex) { return fallback; }
    }

    private static String str(String name, String fallback) {
        try { return data.has(name) ? data.get(name).getAsString() : fallback; }
        catch (Exception ex) { return fallback; }
    }

    public static boolean skyEnabled() { return skyEnabled; }
    public static boolean starsEnabled() { return starsEnabled; }
    public static boolean nebulaEnabled() { return nebulaEnabled; }
    public static boolean planetsEnabled() { return planetsEnabled; }
    public static String theme() { return theme; }
    public static boolean isGalaxyEnabled() { return skyEnabled && "Galaxy".equals(theme); }
    public static void toggleSky() { skyEnabled = !skyEnabled; save(); }
    public static void toggleStars() { starsEnabled = !starsEnabled; save(); }
    public static void toggleNebula() { nebulaEnabled = !nebulaEnabled; save(); }
    public static void togglePlanets() { planetsEnabled = !planetsEnabled; save(); }
    public static void setTheme(String name) { theme = name; save(); }

    private static void save() {
        data.addProperty("skyEnabled", skyEnabled);
        data.addProperty("galaxyEnabled", isGalaxyEnabled());
        data.addProperty("skyTheme", theme.toLowerCase());
        data.addProperty("starsEnabled", starsEnabled);
        data.addProperty("nebulaEnabled", nebulaEnabled);
        data.addProperty("planetsEnabled", planetsEnabled);
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(data), StandardCharsets.UTF_8);
        } catch (Exception ex) { System.err.println("[SkyVibes] Cannot save config: " + ex); }
    }
}
