package nl.skyvibes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Initial client entrypoint for SkyVibes. */
public final class SkyVibesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        createDefaultConfig();
        System.out.println("[SkyVibes] Client initialized. Welcome to the vibe.");
    }

    private void createDefaultConfig() {
        Path config = FabricLoader.getInstance().getConfigDir().resolve("skyvibes.json");
        if (Files.exists(config)) return;

        String defaults = """
                {
                  \"skyTheme\": \"galaxy\",
                  \"lyricsEnabled\": false,
                  \"pvpLyricsEnabled\": false,
                  \"pvpAnimation\": \"circle\",
                  \"font\": \"minecraft\",
                  \"textColor\": \"#9B6CFF\",
                  \"glow\": true,
                  \"animationSpeed\": 1.0
                }
                """;
        try {
            Files.createDirectories(config.getParent());
            Files.writeString(config, defaults, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[SkyVibes] Could not create config: " + e.getMessage());
        }
    }
}
